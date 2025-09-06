package luckkraccoon.family_memory.domain.user.handler;


import luckkraccoon.family_memory.global.error.code.status.BaseErrorCode;
import luckkraccoon.family_memory.global.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode baseErrorCode) { super(baseErrorCode); }
}