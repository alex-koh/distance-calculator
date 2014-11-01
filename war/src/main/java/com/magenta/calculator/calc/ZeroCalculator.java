package com.magenta.calculator.calc;

import com.magenta.calculator.cities.City;

/**
 * Created by alex on 8/16/14.
 */
public class ZeroCalculator implements Calculator {
	/**
	 * Всегда возвращает 0
	 * @param from исходный город
	 * @param to конечный город
	 * @return 0
	 */
	@Override
	public float calc(City from, City to) {
		return 0;
	}
}
