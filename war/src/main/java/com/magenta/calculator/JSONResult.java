package com.magenta.calculator;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import org.apache.struts2.StrutsStatics;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/**
 * Данный класс получает информацию из поля result, формирует на основании
 * неё JSON-объект и отправляет его пользователю.
 */
public class JSONResult implements Result {
    @Override
    public void execute(ActionInvocation ai) throws Exception {
        HttpServletResponse response = (HttpServletResponse)
                ai.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
        response.setContentType("application/json");
		Writer writer = new OutputStreamWriter(response.getOutputStream(), Charset.forName("UTF-8"));
		try {
			Object result = ai.getStack().findValue("result");
			if (result instanceof Collection) {
				new JSONArray((Collection) result).write(writer);
				return;
			}
			if (result instanceof Map) {
				new JSONObject((Map) result).write(writer);
				return;
			}
			new JSONObject(result).write(writer);
		}
		finally {
			writer.flush();
			writer.close();
		}
	}
}
