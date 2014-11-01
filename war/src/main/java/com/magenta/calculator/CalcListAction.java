package com.magenta.calculator;

import com.magenta.calculator.calc.CalculatorFactory;
import com.opensymphony.xwork2.Action;

import com.opensymphony.xwork2.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Данное действие формирует для клиента список доступных вычислителей.
 */
public class CalcListAction implements Action {
    private Map<String,Object> result;
	@Inject
	private CalculatorFactory factory;
    @Override
    public String execute() throws Exception {
        result = new HashMap<String, Object>();
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        for(String s : factory.getNames()) {
            Map<String,Object> calc = new HashMap<String, Object>();
            calc.put("name", s);
            calc.put("description", factory.getDescription(s));
            list.add(calc);
        }
        result.put("list", list);
        return Action.SUCCESS;
    }

    public Map<String, Object> getResult() {
        return result;
    }
}
