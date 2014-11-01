package com.magenta.calculator.data;

import com.magenta.calculator.cities.Distance;

/**
 * Объект доступа к данным. Описывает доступ к матрице расстояний.
 */
public interface DistanceDAO {
	/**
	 * Добавляет расстояние в матрицу.
	 * @param distance информация о расстоянии
	 * @exception Exception
	 */
	void insert(Distance distance) throws Exception;

	/**
	 * Удаляет информацию о расстоянии, определяемое индексами городов
	 * @param from индексы исходного города
	 * @param to индекс конечного города
	 * @exception Exception
	 */
	void delete(Integer from, Integer to) throws Exception;

	/**
	 * Находит расстояние между заданными городами.
	 * @param from начальный город
	 * @param to конечный город
	 * @return расстояние
	 * @exception Exception
	 */
	Float find(Integer from, Integer to) throws Exception;

	/**
	 * Обновляет информацию о расстоянии между городами
	 * @param distance информация о новом расстоянии
	 * @exception Exception
	 */
	void update(Distance distance) throws Exception;
}
