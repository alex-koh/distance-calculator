package com.magenta.calculator;

import com.magenta.calculator.calc.Calculator;
import com.magenta.calculator.calc.CalculatorFactory;
import com.magenta.calculator.cities.City;
import com.magenta.calculator.data.CityDAO;
import com.magenta.calculator.data.DAOFactory;
import com.opensymphony.xwork2.Action;

import com.opensymphony.xwork2.inject.Inject;
import java.util.*;

/**
 * Дейстыие по заданным списоку вычислителей и индексам исходного пункта
 * и пункта назначения производит расчеты и формирует список результатов,
 * хранящий пары (имя_вычислителя, результат).
 */
public class ResultAction implements  Action{
    private Map<String, Object> result;
    @Inject
	private DAOFactory daoFactory;
	@Inject
	private CalculatorFactory calcFactory;
    private List<String> names = new ArrayList<String>();
    private Integer from;
    private Integer to;
    @Override
    public String execute() throws Exception {
        result = new HashMap<String, Object>();
		CityDAO dao = daoFactory.getCityDao();
        City fromCity = dao.find(from);
        City toCity = dao.find(to);

		if (fromCity != null && toCity != null) {
			List<Object> records = new ArrayList<Object>();
			for (String n : names) {
				Calculator c = calcFactory.getCalculator(n);
				if (c != null) {
					Map<String, Object> record = new HashMap<String, Object>();
					record.put("name", c.getClass().getName());
					record.put("value", c.calc(fromCity, toCity));
					records.add(record);
				}
			}
			result.put("result", records);
			return Action.SUCCESS;
		}
		return Action.ERROR;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Map<String, Object> getResult() {
        return result;
    }
}
