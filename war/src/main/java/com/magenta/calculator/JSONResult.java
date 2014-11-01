package com.magenta.calculator;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import org.apache.struts2.StrutsStatics;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Данный класс получает информацию из поля result, формирует на основании
 * неё JSON-объект и отправляет его пользователю.
 */
public class JSONResult implements Result {
    @Override
    public void execute(ActionInvocation ai) throws Exception {
        Map<String,Object> result =
                (Map<String,Object>) ai.getStack().findValue("result");
        JSONObject json = new JSONObject(result);

        HttpServletResponse response = (HttpServletResponse)
                ai.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
        response.setContentType("application/json");

        json.write(response.getWriter());
    }
}
