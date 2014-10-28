/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magenta.calculator;

import com.opensymphony.xwork2.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alex
 */
public class CityListAction implements Action{
	private List<Map<String,Object>> cities;
    private Map<String,Object> result;
	/**
	 * @return Action.SUCCESS
	 * @throws Exception
	 */
    @Override
	public String execute() throws Exception {
        result = new HashMap<String, Object>();
        result.put("size", cities.size());
        result.put("list", cities);
        return Action.SUCCESS;
    }

    public List<Map<String, Object>> getCities() {
        return cities;
    }

    public void setCities(List<Map<String, Object>> cities) {
        this.cities = cities;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}
