package com.magenta.calculator.calc;

import com.magenta.calculator.cities.City;

/**
 Интерфейс вычислителя
 */
public interface Calculator {
	/**
	 * Расчет дистанции между городами
	 * @param from исходный город
	 * @param to конечный город
	 * @return дистанция между городами в километрах
	 */
	float calc(City from, City to);
}
