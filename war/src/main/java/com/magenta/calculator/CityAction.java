package com.magenta.calculator;

import com.magenta.calculator.data.CityDAO;
import com.magenta.calculator.data.DAOFactory;
import com.opensymphony.xwork2.Action;
import com.magenta.calculator.cities.City;

import com.opensymphony.xwork2.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Действие формирует полную информацию о городе по заданному индексу
 */
public class CityAction implements Action {
    private Map<String,Object> result;
    @Inject
	private DAOFactory factory;
    private Integer id;
    @Override
    public String execute() throws Exception {
        result = new HashMap<String, Object>();
		CityDAO dao = factory.getCityDao();
		City city = dao.find(id);
		if (city != null)
        	result.put("city", city);
        return Action.SUCCESS;
    }

	public void setId(Integer id) {
        this.id = id;
    }

    public Map<String,Object> getResult() {
        return result;
    }
}
