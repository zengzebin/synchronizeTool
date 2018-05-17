package common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

public class CtxHelper {

	public static AccData createFailAccData() {
		return new AccData(false, "no", "");
	}

	/**
	 * 
	 * 把AccData打印到JSON(使用toJson)
	 * 
	 * @param ad
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @exception @since
	 *                1.0.0
	 */
	public static void writeToJson(HttpServletResponse response, AccData ad) {
		responseResult(response, ad.toJson(), "UTF-8");
	}

	/**
	 * 响应 ajax提交的请求，返回字符串
	 * 
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public static void responseResult(HttpServletResponse response, String result, String charset) {

		// 1.设定返回类型
		// response.setContentType("text/html;charset="+charset);
		response.setContentType("application/json");
		 
		// 2.取参数
		response.setCharacterEncoding(charset);

		// 3.返回页面
		try {
			response.getOutputStream().write(result.getBytes(charset));
			response.getOutputStream().flush();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("不支持的编码!", e);
		} catch (IOException e) {
			throw new RuntimeException("IO异常!", e);
		}
	}

}
