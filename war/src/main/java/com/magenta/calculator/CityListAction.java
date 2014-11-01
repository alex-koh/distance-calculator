package com.magenta.calculator;

import com.magenta.calculator.data.DAOFactory;
import com.opensymphony.xwork2.Action;

import com.opensymphony.xwork2.inject.Inject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Действие формирует список состоящий из пар (индекс_города, имя_города).
 */
public class CityListAction implements Action{
    private Map<String,Object> result;
	@Inject
	private DAOFactory factory;

    @Override
	public String execute() throws Exception {
		Collection<Map<String,Object>> cities =
				factory.getCityDao().selectNames();
        result = new HashMap<String, Object>();
        result.put("size", cities.size());
        result.put("list", cities);
        return Action.SUCCESS;
    }

    public Map<String, Object> getResult() {
        return result;
    }
}
