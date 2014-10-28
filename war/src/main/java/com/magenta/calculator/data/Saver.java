package com.magenta.calculator.data;

import com.magenta.calculator.cities.City;
import com.magenta.calculator.cities.Distance;

import java.sql.SQLException;

/**
 * Интерфейс класса, сохраняющего информацию в базу данных
 */
public interface Saver {
	/**
	 * Сохраняет информацию о городе
	 * @param city информация о городе
	 * @throws SQLException ошибка доступа к данным
	 */
    void write(City city) throws SQLException;

	/**
	 * Вычисляет отображение индексов загружаемых данных на индексы в БД
	 * @throws Exception ошибка доступа к данным
	 */
    void makeIndex() throws Exception;

	/**
	 * Добавляет информацию о расстояние между городами
	 * @param distance расстояние
	 * @throws SQLException ошибка доступа к данным
	 */
    void write(Distance distance) throws SQLException;

	/**
	 Создает новый объект, обеспечивающий доступ к данным
	 * @return объект для доступа к данным
	 */
	Loader getLoader();
}
