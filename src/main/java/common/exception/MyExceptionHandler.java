package common.exception;

 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

 
import common.util.AccData;
import common.util.CtxHelper;
import common.util.DataUtil;
 

public class MyExceptionHandler implements HandlerExceptionResolver {
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MyExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
	 	AccData ad = CtxHelper.createFailAccData();
		ad.setMsg(DataUtil.getRootCauseMsg(ex));
		
		Map<String, Object> model = new HashMap<String, Object>();
	//	log.error(ex.getMessage(),ex);
		model.put(ex.getMessage(), ex);

		// 根据不同错误转向不同页面
		CtxHelper.writeToJson(response, ad); 
		return new ModelAndView("error");
		 
	}
}