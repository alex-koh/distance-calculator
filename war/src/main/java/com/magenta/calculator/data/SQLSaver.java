package com.magenta.calculator.data;

import com.magenta.calculator.cities.City;
import com.magenta.calculator.cities.Distance;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс отвечает за загрузку данных в базу данных. Сохраняет информацию
 * о городах и расстояниях между ними.
 */
public class SQLSaver implements Saver {
    private Map<Integer, Integer> index;
	private PreparedStatement saveCity;
    private PreparedStatement readIndexes;
	private PreparedStatement saveDistance;

	/**
	 * Подготовка загрузчика к работе
	 * @param dataSource источник базы данных
	 * @throws SQLException ошибка доступа к данным
	 */
	public SQLSaver(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();

		saveCity = connection.prepareStatement(
				"INSERT INTO City " +
                "(key, Name, Latitude, Longitude) " +
				"VALUES (?, ?, ?, ?);");
        readIndexes = connection.prepareStatement(
				"SELECT key,id FROM City;");
		saveDistance = connection.prepareStatement(
				"INSERT INTO Distance " +
				"(FromCity, ToCity, Distance) " +
				"VALUES (?, ?, ?)");
		index = new HashMap<Integer, Integer>();
	}

	/**
	 * Записывает информации о городе в базу данных
	 * @param city информация о городе
	 * @throws SQLException ошибка доступа к данным
	 */
	@Override
    public void write(City city) throws SQLException {
        saveCity.setInt(1, city.getKey());
		saveCity.setString(2,city.getName());
		saveCity.setFloat(3,city.getLatitude());
		saveCity.setFloat(4,city.getLongitude());
		saveCity.execute();

		index.put(city.getKey(),-1);
	}

	/**
	 * Ставит в соответствие каждому ключу из XML-файла последний загруженный
	 * ключ в базе данных.
	 * @throws SQLException ошибка доступа к данным
	 */
    @Override
    public void makeIndex() throws SQLException{
        ResultSet result = readIndexes.executeQuery();
        while (result.next()) {
			if (index.containsKey(result.getInt(1)))
            	index.put(result.getInt(1), result.getInt(2));
        }
    }

	/**
	 * Запись информации о дистанции между городами в базу данных
	 * @param distance дистанция
	 * @throws SQLException
	 */
	@Override
    public void write(Distance distance) throws SQLException {
        Integer from = index.get(distance.getFrom());
        Integer to = index.get(distance.getTo());
        Float value = distance.getDistance();
        if(from != null && to != null && value != null) {
            saveDistance.setInt(1,from);
            saveDistance.setInt(2,to);
            saveDistance.setFloat(3,value);
            saveDistance.execute();
        }
	}

	/**
	 * Возвращает новый экзкмпляр загрузчика информации
	 * @return
	 */
	@Override
	public Loader getLoader() {
		return null; //TODO напиши
	}
}
