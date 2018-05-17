package common.exception;

 
/**
 * 系统业务异常
 * @author zhengxican; Email:zhengxican@126.com
 * @date 2016年3月16日
 * @version V1.0
 */
public class DataAccessException extends RuntimeException {

	private String code;

	public DataAccessException() {
		super();
	}

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(String code, String message) {
		super(message);
		this.code = code;
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataAccessException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
