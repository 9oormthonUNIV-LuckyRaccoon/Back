package luckkraccoon.family_memory.domain.chapter.handler;

import luckkraccoon.family_memory.global.error.code.status.BaseErrorCode;
import luckkraccoon.family_memory.global.exception.GeneralException;

public class ChapterHandler extends GeneralException {
    public ChapterHandler(BaseErrorCode baseErrorCode) { super(baseErrorCode); }
}