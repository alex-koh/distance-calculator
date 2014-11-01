package com.magenta.calculator.calc;

import com.magenta.calculator.cities.City;
import com.magenta.calculator.data.DAOFactory;
import com.magenta.calculator.data.DistanceDAO;
import com.magenta.calculator.data.CityDAO;

import java.util.Map;

/**
 Вычислитель, определяющий расстояние между двумя городами через матрицу
 расстояний
 */
public class DistanceMatrix implements Calculator {
	private DistanceDAO distanceDAO;

	public DistanceMatrix(DAOFactory factory) throws Exception {
		this.distanceDAO = factory.getDistanceDao();
	}

	/**
	 * Для заданных городов находит в БД расстояние между ними
	 * @param from исходный город
	 * @param to конечный город
	 * @return возвращает расстояние из базы данных или Flaot.MAX_VALUE,
	 * если расстояние не найдено
	 */
	@Override
	public float calc(City from, City to) {
		try {
			return distanceDAO.find(from.getKey(), to.getKey());
		}
		catch (Exception exc) {	}
		return Float.MAX_VALUE;
	}
}
