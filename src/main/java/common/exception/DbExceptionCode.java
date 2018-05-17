package common.exception;

 

import java.text.MessageFormat;

public enum DbExceptionCode implements ExceptionCode { 
    UNIQUE_KEY(10001, "主键约束错误"), 

    FOREIGN_KEY(10002, "外键约束错误"),
	FOREIGN_KEY2(12, "外键约束错误"); 

    public final int errorCode; 
    public final String errorName; 

    private DbExceptionCode(int errorCode, String errorName) { 
        this.errorCode = errorCode; 
        this.errorName = errorName; 
    } 

    public int getErrorCode() { 
        return errorCode; 
    } 

    public String getErrorCause(Object... args) { 
        // TODO need to read from configuration by errorCode 
        String errorCause = ""; 
        return MessageFormat.format(errorCause, args); 
    } 
}	

