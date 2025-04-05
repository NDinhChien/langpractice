package com.ndinhchien.langPractice.domain.pack.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ndinhchien.langPractice.domain.history.entity.History;
import com.ndinhchien.langPractice.domain.history.repository.HistoryRepository;
import com.ndinhchien.langPractice.domain.pack.dto.PackRequestDto.CreatePackDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackRequestDto.UpdatePackDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackResponseDto.IPackBasicDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackResponseDto.IPackDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackResponseDto.IPackSearchDto;
import com.ndinhchien.langPractice.domain.pack.dto.PackResponseDto.PackBasicDto;
import com.ndinhchien.langPractice.domain.pack.entity.Pack;
import com.ndinhchien.langPractice.domain.pack.entity.Save;
import com.ndinhchien.langPractice.domain.pack.entity.type.LanguageType;
import com.ndinhchien.langPractice.domain.pack.repository.PackRepository;
import com.ndinhchien.langPractice.domain.pack.repository.SaveRepository;
import com.ndinhchien.langPractice.domain.user.entity.User;
import com.ndinhchien.langPractice.domain.user.repository.UserRepository;
import com.ndinhchien.langPractice.global.dto.PageDto;
import com.ndinhchien.langPractice.global.exception.BusinessException;
import com.ndinhchien.langPractice.global.exception.ErrorMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PackService {

    private final UserRepository userRepository;
    private final PackRepository packRepository;
    private final SaveRepository saveRepository;
    private final HistoryRepository historyRepository;

    public PageDto<?> search(String keywords, int pageSize, int page, String sortBy, String sortOrder) {
        Sort sortDetails = StringUtils.hasText(keywords) ? Sort.by(Direction.DESC, "rank")
                : Sort.by(Direction.valueOf(sortOrder.toUpperCase()), sortBy);

        Pageable pageDetails = PageRequest.of(Math.max(0, page - 1), pageSize, sortDetails);
        if (StringUtils.hasText(keywords)) {
            Page<IPackSearchDto> result = packRepository.searchByKeywords(keywords, pageDetails);
            return new PageDto<>(result.getContent(), result.getSize(), result.getNumber(), result.getTotalPages(),
                    (int) result.getTotalElements());
        }
        Page<IPackBasicDto> result = packRepository.findAllByIsPublishedAndIsDeleted(pageDetails, true, false);
        return new PageDto<>(result.getContent(), result.getSize(), result.getNumber(), result.getTotalPages(),
                (int) result.getTotalElements());
    }

    public List<?> getPacks(User owner, boolean getFull) {
        if (getFull) {
            return packRepository.findAllByOwner(owner);
        }
        return packRepository.findAllByOwner(owner, PackBasicDto.class);
    }

    public Map<String, Object> getPublishedPacksOfUsers(List<Long> userIds) {
        Map<Long, List<IPackBasicDto>> result = new HashMap<>();
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            User owner = userRepository.findById(userId).orElse(null);
            if (owner == null)
                continue;
            List<IPackBasicDto> packs = packRepository.findAllByOwnerAndIsPublishedAndIsDeleted(owner, true, false);
            result.put(userId, packs);
            users.add(owner);
        }
        return Map.of("packs", result, "users", users);
    }

    public Object getPackById(User user, Long id, boolean getFull) {
        if (getFull) {
            IPackDto pack = getPackFull(id);
            if (canGetPack(user, pack.getIsPublished(), pack.getIsDeleted(), pack.getOwnerId())) {
                return pack;
            }
        }
        PackBasicDto pack = getPack(id);
        if (canGetPack(user, pack.isPublished(), pack.isDeleted(), pack.ownerId())) {
            return pack;
        }
        return null;
    }

    public PackBasicDto getPack(Long id) {
        return packRepository.findById(id, PackBasicDto.class).orElseThrow(
                () -> {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.PACK_NOT_FOUND);
                });
    }

    public IPackDto getPackFull(Long id) {
        return packRepository.findPackById(id).orElseThrow(
                () -> {
                    throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.PACK_NOT_FOUND);
                });
    }

    @Transactional
    public Pack createPack(
            User owner,
            CreatePackDto requestDto) {
        String name = requestDto.getName();
        String description = requestDto.getDescription();
        String keywords = requestDto.getKeywords();
        LanguageType srcLang = requestDto.getSrcLang();
        LanguageType desLang = requestDto.getDesLang();
        Pack pack = Pack.builder()
                .owner(owner)
                .name(name)
                .description(description)
                .keywords(keywords)
                .srcLang(srcLang)
                .desLang(desLang)
                .build();
        Boolean isPublished = requestDto.getIsPublished();
        if (isPublished != null) {
            pack.setIsPublished(isPublished);
        }

        return packRepository.save(pack);
    }

    public Pack updatePack(User user, Long packId, UpdatePackDto requestDto) {
        Pack pack = validatePackIsOwner(packId, user);
        pack.update(requestDto);
        return packRepository.save(pack);
    }

    @Transactional
    public Save toggleSave(User user, Long packId) {
        Pack pack = validatePackByUser(user, packId);
        if (!pack.isOwner(user)) {
            Boolean saved = pack.saveBy(user);
            packRepository.save(pack);
            History history = historyRepository.findByUserAndPackId(user, packId).orElse(null);
            Save save = saveRepository.findByUserAndPack(user, pack).orElse(null);
            if (history == null) {
                history = new History(user, pack.getId());
                historyRepository.save(history);
            }
            if (save == null) {
                save = new Save(user, pack);
            }
            save.setIsDeleted(!saved);
            return saveRepository.save(save);
        }
        return null;
    }

    @Transactional
    public boolean toggleLike(User user, Long packId) {
        Pack pack = validatePackByUser(user, packId);
        boolean liked = pack.likeBy(user);
        packRepository.save(pack);
        return liked;
    }

    public Pack validatePackIsOwner(Long packId, User user) {
        return packRepository.findByIdAndOwner(packId, user).orElseThrow(() -> {
            throw new BusinessException(HttpStatus.FORBIDDEN, ErrorMessage.PACK_OWNER_ONLY);
        });
    }

    public Pack validatePackByUser(User user, Long id) {
        Pack pack = validatePack(id);
        if (canGetPack(user, pack)) {
            return pack;
        }
        throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.PACK_IS_PRIVATE);
    }

    public Pack validatePack(Long id) {
        return packRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.PACK_NOT_FOUND);
        });
    }

    private Boolean canGetPack(@Nullable User user, Pack pack) {
        if (pack.getIsPublished() && !pack.getIsDeleted())
            return true;
        if (user == null) {
            return false;
        }

        if (user.getId().equals(pack.getOwnerId()))
            return true;
        return false;
    }

    private boolean canGetPack(User user, Boolean isPublished, Boolean isDeleted, Long ownerId) {
        if (isPublished && !isDeleted)
            return true;
        if (user == null) {
            return false;
        }
        if (user.getId().equals(ownerId))
            return true;
        return false;
    }
}
