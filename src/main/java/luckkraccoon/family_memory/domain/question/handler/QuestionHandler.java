// src/main/java/luckkraccoon/family_memory/domain/question/handler/QuestionHandler.java
package luckkraccoon.family_memory.domain.question.handler;

import luckkraccoon.family_memory.global.error.code.status.BaseErrorCode;
import luckkraccoon.family_memory.global.exception.GeneralException;

public class QuestionHandler extends GeneralException {
    public QuestionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}