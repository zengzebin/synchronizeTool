package common.exception;

 
public interface ExceptionCode { 
/** 
* 返回异常码 
*/ 
    int getErrorCode(); 
    
/** 
* 返回异常描述 
* @param args 异常描述所用到的占位符值数组 
*/	    
    String getErrorCause(Object... args); 
}	

