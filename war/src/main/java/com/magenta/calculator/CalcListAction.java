package com.magenta.calculator;

import com.magenta.calculator.calc.CalculatorFactory;
import com.opensymphony.xwork2.Action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.inject.Inject;

import java.util.*;

/**
 * Данное действие формирует для клиента список доступных вычислителей.
 */
public class CalcListAction implements Action {
    private Collection<Map<String,Object>> result;
	@Inject
	private CalculatorFactory factory;
    @Override
    public String execute() throws Exception {
		ResourceBundle bundle = ResourceBundle.getBundle("shortNames",
				ActionContext.getContext().getLocale());

		result =  new ArrayList<Map<String, Object>>();
        for(String s : factory.getNames()) {
            Map<String,Object> calc = new HashMap<String, Object>();
            calc.put("name", s);
            calc.put("shortName", bundle.getString(s));
            result.add(calc);
        }
        return Action.SUCCESS;
    }

    public Collection<Map<String, Object>> getResult() {
        return result;
    }
}
