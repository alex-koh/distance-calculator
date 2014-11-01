package com.magenta.calculator.data;

import com.magenta.calculator.cities.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map;


/**
 * Объект доступа к данным. Реализует доступ к таблице City, хранящейся в БД.
 */
public class SQLCityDAO implements CityDAO,SQLDAOSettable {
	private PreparedStatement saver;
	private PreparedStatement loader;
	private PreparedStatement selectNames;
	private Connection connection;

	/**
	 * Конструктор для заданного соединения создает объект, обеспечивающий
	 * доступ к таблице City
	 * @param connection
	 * @throws SQLException
	 */
	public void setConnection(Connection connection) throws SQLException{
		this.connection = connection;
		saver = connection.prepareStatement(
				"INSERT INTO City " +
				"(id_key, Name, Latitude, Longitude) " +
				"VALUES (?, ?, ?, ?);");
		loader = connection.prepareStatement(
				"SELECT id_key,name,Latitude,Longitude " +
				"FROM City " +
				"WHERE id=(" +
					"select max(id) " +
					"from City " +
					"where id_key=?" +
				");");
		selectNames = connection.prepareStatement(
				"select id_key, Name " +
				"from City " +
				"where id in (" +
					"select max(id) " +
					"from City " +
					"group by id_key " +
				") " +
				"order by id_key;");
	}

	/**
	 * Добавляет новую информацию о городе. Индекс города не является
	 * первичным, ключом и может дублироваться.
	 * @param city структура хранит информацию о городе
	 * @throws SQLException
	 */
	@Override
	public void insert(City city) throws SQLException{
		saver.setInt(1, city.getKey());
		saver.setString(2, city.getName());
		saver.setFloat(3, city.getLatitude());
		saver.setFloat(4, city.getLongitude());
		saver.execute();
	}

	/**
	 * Данный метод не реализован и генерирует исключение
	 * {@link java.lang.UnsupportedOperationException}
	 * @param id индекс, заданный при загрузке информации о городе
	 */
	@Override
	public void delete(Integer id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * По заданному индексу города находит в таблице City последнюю добавленную
	 * информацию о городе. Выбирается информация с наибольшим первичным ключом.
	 * @param id индекс
	 * @return последняя запись о городе
	 * @throws SQLException
	 */
	@Override
	public City find(Integer id) throws SQLException{
		loader.setInt(1, id);
		ResultSet result = loader.executeQuery();
		if (result.next()) {
			City city = new City();
			city.setKey(result.getInt(1));
			city.setName(result.getString(2));
			city.setLatitude(result.getFloat(3));
			city.setLongitude(result.getFloat(4));
			return city;
		}
		return null;
	}

	/**
	 * Данный метод не реализован и генерирует исключение
	 * {@link java.lang.UnsupportedOperationException}
	 * @param city Новая информация о городе
	 */
	@Override
	public void update(City city) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Список городов с их индексами. В список помещаются информация о
	 * последних записях для каждого города.
	 * @return Упорядоченный по возрастанию индекса список городов
	 * @throws SQLException
	 */
	@Override
	public Collection<Map<String, Object>> selectNames() throws SQLException{
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		ResultSet resultSet = selectNames.executeQuery();
		while (resultSet.next()) {
			Map<String, Object> city = new HashMap<String, Object>();
			city.put("index", resultSet.getInt(1));
			city.put("name", resultSet.getString(2));
			result.add(city);
		}
		return result;
	}

	/**
	 * Закрытие соединения
	 * @throws Throwable
	 */
	@Override
	protected void finalize() throws Throwable {
		connection.close();
		super.finalize();
	}
}
