package com.magenta.calculator;

import com.magenta.calculator.calc.CalculatorFactory;
import com.opensymphony.xwork2.Action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CalcListAction implements Action {
    private CalculatorFactory calcFactory;
    private Map<String,Object> result;
    @Override
    public String execute() throws Exception {
        result = new HashMap<String, Object>();
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        for(String s : calcFactory.getNames()) {
            Map<String,Object> calc = new HashMap<String, Object>();
            calc.put("name", s);
            calc.put("description", calcFactory.getDescription(s));
            list.add(calc);
        }
        result.put("list", list);
        return Action.SUCCESS;
    }

    public CalculatorFactory getCalcFactory() {
        return calcFactory;
    }

    public void setCalcFactory(CalculatorFactory calcFactory) {
        this.calcFactory = calcFactory;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}
