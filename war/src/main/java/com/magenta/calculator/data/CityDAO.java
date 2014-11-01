package com.magenta.calculator.data;

import com.magenta.calculator.cities.City;

import java.util.Collection;
import java.util.Map;

/**
 * Объект доступа к данным. Предоставляет доступ к таблице City.
 */
public interface CityDAO {
	/**
	 * Добавляет информацию о заданном городе в БД.
	 * @param city структура хранит информацию о городе
	 * @throws Exception
	 */
	void insert(City city) throws Exception;

	/**
	 * По заданному индексу производит удаление объекта из базы
	 * @param id индекс, заданный при загрузке информации о городе
	 * @throws Exception
	 */
	void delete(Integer id) throws Exception;

	/**
	 * Находит информацию о городе по заданному индексу.
	 * @param id индекс
	 * @return Возвращает структуру, хранящую информацию о городе.
	 * @throws Exception
	 */
	City find(Integer id) throws Exception;

	/**
	 * Выполняет обновление информации о городе.
	 * @param city Новая информация о городе
	 * @throws Exception
	 */
	void update(City city) throws Exception;

	/**
	 * Возвращает список всех городов и соответствующих им индексов
	 * @return отсортированный по возрастанию индекса список пар
	 * (индекс_города, название_города).
	 * @throws Exception
	 */
	Collection<Map<String,Object>> selectNames() throws Exception;
}
