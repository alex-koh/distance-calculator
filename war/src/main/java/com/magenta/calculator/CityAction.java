package com.magenta.calculator;

import com.magenta.calculator.data.CityDAO;
import com.magenta.calculator.data.DAOFactory;
import com.opensymphony.xwork2.Action;

import com.opensymphony.xwork2.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Действие формирует полную информацию о городе по заданному индексу
 */
public class CityAction implements Action {
    private Object result;
    @Inject
	private DAOFactory factory;
    private Integer key;
    @Override
    public String execute() throws Exception {
        result = new HashMap<String, Object>();
		CityDAO dao = factory.getCityDao();
		result = dao.find(key);
        return Action.SUCCESS;
    }

	public void setKey(Integer key) {
		this.key = key;
	}

	public Object getResult() {
        return result;
    }
}
