package com.magenta.calculator.data;

import com.magenta.calculator.cities.City;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

/**
 * Класс отвечает за загрузку информации из базы. Предоставляет три вида
 * результатов: список всех городов с их индексами, информацию о конкретном
 * городе и расстояние между двумя городами
 */
public class CityLoader implements Loader {
    private DataSource dataSource;
	private Map<Integer,String> cities;
	private Map<Integer,Integer> index;

	/**
	 * Загружает список доступных городов и строит отбражение исходного индекса
	 * города на индекс города в БД.
	 * @param dataSource источник данных
	 * @throws SQLException ошибка доступа к данным
	 */
    public CityLoader(DataSource dataSource) throws SQLException{
        this.dataSource = dataSource;
		cities = new HashMap<Integer, String>();
		index = new HashMap<Integer, Integer>();
		Connection connection = dataSource.getConnection();
		try {
			PreparedStatement st = connection.prepareStatement(
					"SELECT key,id,name FROM City;");
			ResultSet result = st.executeQuery();
			while (result.next()) {
				cities.put(result.getInt(1),result.getString(3));
				index.put(result.getInt(1),result.getInt(2));
			}
			cities = Collections.unmodifiableMap(cities);
			index = Collections.unmodifiableMap(index);
		}
		finally {
			connection.close();
		}
    }

	/**
	 * Возвращает информацию о городе, по его индексу в текущем списке городов
	 * @param id индекс города текущей версии списка городов
	 * @return Возвращает информацию в виде объекта javabean или null, если
	 * такой город не найден
	 * @throws SQLException
	 */
    @Override
	public City load(Integer id) throws SQLException {
		Connection connection = dataSource.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
				"SELECT key,name,Latitude,Longitude FROM City WHERE (id=?)"
			);
			id = index.get(id);
			if (id!=null) {
				statement.setInt(1, id);
				ResultSet result = statement.executeQuery();
				if (result.next()) {
					City city = new City();
					city.setKey(result.getInt(1));
					city.setName(result.getString(2));
					city.setLatitude(result.getFloat(3));
					city.setLongitude(result.getFloat(4));
					return city;
				}
			}
		}
		finally {
			connection.close();
		}
		return null;
    }

	/**
	 * Получает из БД дистанцию между двумя городами, заданными своими индексами
	 * @param from начальный город
	 * @param to конечный город
	 * @return дистанция. Если дистанция не найдена, то возвращается
	 * Float.MAX_VALUE
	 */
	@Override
	public Float load(Integer from, Integer to) {
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement statement = connection.prepareStatement(
				"SELECT Distance FROM Distance WHERE (FromCity=?) and " +
							"(ToCity=?);"
			);
			from = index.get(from);
			to = index.get(to);
			if (from != null && to != null) {
				statement.setInt(1,from);
				statement.setInt(2,to);
				ResultSet result = statement.executeQuery();
				if (result.next()) {
					return result.getFloat(1);
				}
			}
		}
		catch (SQLException exc) { }
		return Float.MAX_VALUE;
	}

	/**
	 * Возвращает список городов текущей версии загрузчика.
	 * @return неизменяемый список имен городов с их уникальными индексами.
	 */
	@Override
	public Map<Integer,String> load() {
		return cities;
	}
}
