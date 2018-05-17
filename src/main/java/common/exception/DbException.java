package common.exception;

 
public class DbException extends RuntimeException { 

    protected int errorCode; 

    protected String errorCause; 

    /** 
     * message format：BussinessErrorCode:BussinessErrorCause eg: 10010:无效Barcode[1000052] 
     * 
     * @param errorCode 
     *            error code of bussiness 
     * @param errorCause 
     *            error cause of bussiness 
     * @param cause 
     */ 
    protected DbException(int errorCode, String errorCause, Throwable cause) { 
        super(errorCode + ":" + errorCause, cause); 
        this.errorCode = errorCode; 
        this.errorCause = errorCause; 
    } 

    public DbException(ExceptionCode exceptionCode, Object... args) { 
        this(exceptionCode.getErrorCode(), exceptionCode.getErrorCause(args)); 
    } 

    public DbException(Throwable cause, ExceptionCode exceptionCode, Object... args) { 
        this(exceptionCode.getErrorCode(), exceptionCode.getErrorCause(args), cause); 
    } 

    public DbException(ExceptionCode exceptionCode, Throwable cause) { 
        this(exceptionCode.getErrorCode(), exceptionCode.getErrorCause(), cause); 
    } 

    protected DbException(int errorCode, String errorCause) { 
        this(errorCode, errorCause, null); 
    } 

    /** 
     * @return the errorCode 
     */ 
    public int getErrorCode() { 
        return errorCode; 
    } 

    /** 
     * @return the errorCause 
     */ 
    public String getErrorCause() { 
        return errorCause; 
    } 

}	