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
    private Collection<Map<String,Object>> result;
	@Inject
	private DAOFactory factory;

    @Override
	public String execute() throws Exception {
        result = factory.getCityDao().selectNames();
        return Action.SUCCESS;
    }

    public Collection<Map<String, Object>> getResult() {
        return result;
    }
}
