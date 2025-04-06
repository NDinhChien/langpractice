package com.ndinhchien.langPractice.global.exception;

public class ErrorMessage {
    public static final String USER_NOT_FOUND = "User does not exist.";
    public static final String PASSWORD_MISMATCH = "Confirm password does not match.";
    public static final String EMAIL_REGISTERED = "This email has been registered, please use another email";
    public static final String USERNAME_EXISTED = "Username already exists, please choose another one";

    public static final String INCORRECT_PASSWORD = "Incorrect password";

    public static final String PACK_NOT_FOUND = "Pack does not exist.";
    public static final String PACK_OWNER_ONLY = "You do not own this pack";
    public static final String PACK_IS_PRIVATE = "You do not have access to this pack";

    public static final String HISTORY_NOT_FOUND = "History entity does not exist.";
    public static final String IMAGE_NOT_FOUND = "Could not find image";

    public static final String UNSUPPORTED_FILE_TYPE = "This file type is not supported.";
    public static final String FILE_SIZE_EXCEEDED = "File's size exceeds limit";

    public static final String QUESTION_NOT_FOUND = "Question does not exist.";
    public static final String QUESTION_OWNER_ONLY = "You do not own this question";
    public static final String INVALID_PAYLOAD_UPDATE_QUESTION_LIST = "Can not update questions of different packs at once";
}
