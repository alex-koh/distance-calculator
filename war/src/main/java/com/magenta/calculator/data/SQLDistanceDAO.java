package com.magenta.calculator.data;

import com.magenta.calculator.cities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Объект доступа к данным. Определяет расстояние между городами по матрице
 * расстояний, хранящийся в БД.
 */
public class SQLDistanceDAO implements DistanceDAO, SQLDAOSettable {
	private PreparedStatement saver;
	private PreparedStatement selector;
	private Connection connection;

	/**
	 * Создает для заданного соединения объект доступа к таблице расстояний.
	 * @param connection
	 * @throws SQLException
	 */
	@Override
	public void setConnection(Connection connection) throws SQLException{
		this.connection = connection;
		saver = connection.prepareStatement(
				"INSERT INTO Distance " +
						"(FromCity, ToCity, Distance) " +
						"VALUES (?, ?, ?)");
		selector = connection.prepareStatement(
				"SELECT Distance " +
				"FROM Distance " +
				"WHERE id = (" +
					"select max(id)" +
					"from Distance " +
					"where (FromCity=?) and (ToCity=?)" +
				");");
	}

	/**
	 * Добавляет информацию о расстоянии между городами, заданными их индексами.
	 * Пара индексов может быть не уникальна для таблицы.
	 * @param distance информация о расстоянии
	 * @throws SQLException
	 */
	@Override
	public void insert(Distance distance)  throws SQLException {
		saver.setInt(1, distance.getFrom());
		saver.setInt(2, distance.getTo());
		saver.setFloat(3, distance.getDistance());
		saver.execute();
	}

	/**
	 * Данный метод не реализован. Его вызов приводит к генерации исключения
	 * {@link java.lang.UnsupportedOperationException}.
	 * @param from индексы исходного города
	 * @param to индекс конечного города
	 */
	@Override
	public void delete(Integer from, Integer to) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Находит расстояния между городами по их индексам. Возвращает последнее
	 * добавленное расстояние для данной пары индексов (расстояние с наибольшим
	 * первичным ключом).
	 * @param from начальный город
	 * @param to конечный город
	 * @return расстояние или {@link java.lang.Float}.MAX_VALUE
	 * @throws Exception
	 */
	@Override
	public Float find(Integer from, Integer to) throws Exception{
		if (from != null && to != null) {
			selector.setInt(1,from);
			selector.setInt(2,to);
			ResultSet result = selector.executeQuery();
			if (result.next()) {
				return result.getFloat(1);
			}
		}
		return Float.MAX_VALUE;
	}

	/**
	 * Данный метод не реализован. Его вызов приводит к генерации исключения
	 * {@link java.lang.UnsupportedOperationException}.
	 * @param distance информация о новом расстоянии
	 */
	@Override
	public void update(Distance distance) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Закрывает соединение.
	 * @throws Throwable
	 */
	@Override
	protected void finalize() throws Throwable {
		if (connection != null)
			connection.close();
		super.finalize();
	}
}
