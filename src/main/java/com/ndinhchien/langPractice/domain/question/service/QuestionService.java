package com.ndinhchien.langPractice.domain.question.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ndinhchien.langPractice.domain.pack.entity.Pack;
import com.ndinhchien.langPractice.domain.pack.repository.PackRepository;
import com.ndinhchien.langPractice.domain.pack.service.PackService;
import com.ndinhchien.langPractice.domain.question.dto.QuestionRequestDto.AddQuestionDto;
import com.ndinhchien.langPractice.domain.question.dto.QuestionRequestDto.UpdateQuestionDto;
import com.ndinhchien.langPractice.domain.question.dto.QuestionResponseDto.IQuestionDto;
import com.ndinhchien.langPractice.domain.question.dto.QuestionResponseDto.QuestionDto;
import com.ndinhchien.langPractice.domain.question.entity.Question;
import com.ndinhchien.langPractice.domain.question.entity.Favourite;
import com.ndinhchien.langPractice.domain.question.repository.QuestionRepository;
import com.ndinhchien.langPractice.domain.question.repository.FavouriteRepository;
import com.ndinhchien.langPractice.domain.user.entity.User;
import com.ndinhchien.langPractice.global.exception.BusinessException;
import com.ndinhchien.langPractice.global.exception.ErrorMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final FavouriteRepository favouriteRepository;
    private final PackRepository packRepository;
    private final PackService packService;

    @Transactional
    private Question createQuestion(Pack pack, AddQuestionDto payload) {
        Question question = new Question(pack, payload.getQuestion(), payload.getAnswer());
        return questionRepository.save(question);
    }

    @Transactional
    private List<Question> createQuestions(Pack pack, List<AddQuestionDto> requesDto) {
        return requesDto.stream().map((payload) -> {
            return createQuestion(pack, payload);
        }).toList();
    }

    public List<IQuestionDto> getQuestions(List<Long> packIds, Instant lastGet) {
        return questionRepository.findInPacksAndUpdatedAtBefore(packIds, lastGet);
    }

    @Transactional
    public Question updateQuestion(User user, Long questionId, UpdateQuestionDto requestDto) {
        Question question = validateQuestion(questionId);
        if (!question.getOwnerId().equals(user.getId())) {
            throw new BusinessException(HttpStatus.FORBIDDEN, ErrorMessage.QUESTION_OWNER_ONLY);
        }

        Pack pack = packService.validatePack(question.getPackId());
        Boolean isNewDeleted = requestDto.getIsDeleted();
        if (isNewDeleted != null && isNewDeleted != question.getIsDeleted()) {
            pack.setTotal(Math.max(0, pack.getTotal() + (isNewDeleted ? -1 : 1)));
        }
        question.update(requestDto);
        return questionRepository.save(question);
    }

    @Transactional
    public List<Question> updateQuestions(User user, List<UpdateQuestionDto> requestDtos) {
        List<Question> result = new ArrayList<>();
        for (UpdateQuestionDto requestDto : requestDtos) {
            try {
                result.add(updateQuestion(user, requestDto.getId(), requestDto));
            } catch (Exception e) {
                log.info("Fail to update question {}", requestDto.getId());
            }
        }
        return result;
    }

    @Transactional
    public Favourite toggleFavourite(User user, Long questionId) {
        Question question = validateQuestion(questionId);
        Favourite favourite = favouriteRepository.findByUserAndQuestionId(user, questionId).orElse(
                new Favourite(user, question));
        favourite.setIsDeleted(!favourite.getIsDeleted());
        return favouriteRepository.save(favourite);
    }

    @Transactional
    public List<Question> addQuestions(User user, Long packId, List<AddQuestionDto> requestDto) {
        Pack pack = packService.validatePackIsOwner(packId, user);
        List<Question> questions = createQuestions(pack, requestDto);
        pack.setTotal(pack.getTotal() + questions.size());
        packRepository.save(pack);
        return questions;
    }

    public List<QuestionDto> getQuestions(User user, Long packId) {
        Pack pack = packService.validatePackByUser(user, packId);
        return questionRepository.findAllByPack(pack);
    }

    private Question validateQuestion(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> {
            throw new BusinessException(HttpStatus.BAD_REQUEST, ErrorMessage.QUESTION_NOT_FOUND);
        });
    }

}
