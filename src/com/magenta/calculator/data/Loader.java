package com.magenta.calculator.data;

import com.magenta.calculator.cities.City;

import java.sql.SQLException;
import java.util.Map;

/**
 * Created by alex on 10/10/14.
 */
public interface Loader {
	City load(Integer id) throws SQLException;

	Float load(Integer from, Integer to);

	Map<Integer,String> load();
}
