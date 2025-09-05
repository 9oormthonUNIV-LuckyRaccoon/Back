package luckkraccoon.family_memory.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import luckkraccoon.family_memory.global.error.code.status.BaseErrorCode;
import luckkraccoon.family_memory.global.error.code.status.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private BaseErrorCode code;
    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }
    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}