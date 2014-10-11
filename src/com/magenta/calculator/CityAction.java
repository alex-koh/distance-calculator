package com.magenta.calculator;

import com.magenta.calculator.data.CityLoader;
import com.opensymphony.xwork2.Action;
import org.apache.tomcat.jdbc.pool.DataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 9/4/14.
 */
public class CityAction implements Action {
    private Map<String,Object> result;
    private CityLoader loader;
    private Integer id;
    @Override
    public String execute() throws Exception {
        result = new HashMap<String, Object>();
		City city = loader.load(id);
		city.setKey(id);
        result.put("city", city);
        return Action.SUCCESS;
    }

	public CityLoader getLoader() {
		return loader;
	}

	public void setLoader(CityLoader loader) {
		this.loader = loader;
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public Map<String,Object> getResult() {
        return result;
    }
}
