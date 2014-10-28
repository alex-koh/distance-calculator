package com.magenta.calculator.calc;

import com.magenta.calculator.cities.City;
import com.magenta.calculator.data.Loader;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

/**
 Вычислитель, определяющий расстояние между двумя городами через матрицу
 расстояний
 */
public class DistanceMatrix implements Calculator {
	private Loader loader;

	public DistanceMatrix(Loader loader) {
		this.loader = loader;
	}

	@Override
	public String getName() {
		return "Distance Matrix";
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
		return loader.load(from.getKey(), to.getKey());
	}
}
