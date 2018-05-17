package common.util;

 

import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpSession;  
  
/** 
 * token处理器
 * @author zhengxican 
 * @email zhengxican@126.com 
 * 
 */  
public class TokenProcessor {  
  
	private static final String TOKEN_LIST_NAME = "tokenList";

	public static final String TOKEN_STRING_NAME = "token";
	
    private long previous; 
    
    private static TokenProcessor instance = new TokenProcessor(); 
    
    protected TokenProcessor() {  
    }  
  
    public static TokenProcessor getInstance() {  
        return instance;  
    } 
  
    /**
     * 验证session中保存的token和request中的参数"token"是否相等
     * @param request
     * @return 匹配结果
     */
    public synchronized boolean isTokenValid(HttpServletRequest request) {  
        return isTokenValid(request, false);  
    }  
  /**
   * 验证session中保存的token和request中的参数"token"是否相等，并选择是否移除session中的token
   * @param request
   * @param reset
   * @return 匹配结果
   */
	public synchronized boolean isTokenValid(HttpServletRequest request, boolean reset) {

		HttpSession session = request.getSession(false);
		if (session == null)
			return false;
		String saved = (String) session.getAttribute(TOKEN_STRING_NAME);
		if (saved == null)
			return false;

		if (reset)
			resetToken(request);
		String token = request.getParameter(TOKEN_STRING_NAME);
		if (token == null)
			return false;
		else
			return saved.equals(token);
	}
  
  /**
   * 移除session中的token
   * @param request
   */
    public synchronized void resetToken(HttpServletRequest request) {  
        HttpSession session = request.getSession(false);  
        if (session == null) {  
            return;  
        } else {  
            session.removeAttribute(TOKEN_STRING_NAME);  
            return;  
        }  
    }  
  
  /**
   * 根据sessionId生成token并保存到session中
   * @param request
   */
	public synchronized void saveToken(HttpServletRequest request) {
		saveToken(request, null);
	}
	
    /**
     * 保存token到session中
     * @param request
     * @param token串
     */
	public synchronized void saveToken(HttpServletRequest request, String token) {
		if (null == token) {
			token = generateToken(request);
		}
		if (token != null) {
			HttpSession session = request.getSession();
			session.setAttribute(TOKEN_STRING_NAME, token);
		}
	}
  
	/**
	 * 根据sessionId生成token
	 * @param request
	 * @return token串
	 */
    public synchronized String generateToken(HttpServletRequest request) {  
        HttpSession session = request.getSession();  
        return generateToken(session.getId());  
    }  
  
	/**
	 * 根据id生成token
	 * @param id
	 * @return token串
	 */
	public synchronized String generateToken(String id) {
		return generateToken(id, null);
	}

	/**
	 * 根据id1和id2生成token
	 * @param id1
	 * @param id2
	 * @return token串
	 */
	public synchronized String generateToken(String id1, String id2) {
		try {

			long current = System.currentTimeMillis();
			if (current == previous)
				current++;
			previous = current;

			byte now[] = (new Long(current)).toString().getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (null != id1) {
				md.update(id1.getBytes());
			}
			if (null != id2) {
				md.update(id2.getBytes());
			}
			md.update(now);
			return toHex(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * 转化为16进制
	 * @param buffer
	 * @return 16进制字符串
	 */
    private String toHex(byte buffer[]) {  
        StringBuffer sb = new StringBuffer(buffer.length * 2);  
        for (int i = 0; i < buffer.length; i++) {  
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));  
            sb.append(Character.forDigit(buffer[i] & 15, 16));  
        }  
  
        return sb.toString();  
    }  
}  