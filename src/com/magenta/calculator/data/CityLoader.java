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
 * ����� �������� �� �������� ���������� �� ����. ������������� ��� ����
 * �����������: ������ ���� ������� � �� ���������, ���������� � ����������
 * ������ � ���������� ����� ����� ��������
 */
public class CityLoader implements Loader {
    private DataSource dataSource;
	private Map<Integer,String> cities;
	private Map<Integer,Integer> index;

	/**
	 * ��������� ������ ��������� ������� � ������ ���������� ��������� �������
	 * ������ �� ������ ������ � ��.
	 * @param dataSource �������� ������
	 * @throws SQLException ������ ������� � ������
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
	 * ���������� ���������� � ������, �� ��� ������� � ������� ������ �������
	 * @param id ������ ������ ������� ������ ������ �������
	 * @return ���������� ���������� � ���� ������� javabean ��� null, ����
	 * ����� ����� �� ������
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
	 * �������� �� �� ��������� ����� ����� ��������, ��������� ������ ���������
	 * @param from ��������� �����
	 * @param to �������� �����
	 * @return ���������. ���� ��������� �� �������, �� ������������
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
	 * ���������� ������ ������� ������� ������ ����������.
	 * @return ������������ ������ ���� ������� � �� ����������� ���������.
	 */
	@Override
	public Map<Integer,String> load() {
		return cities;
	}
}
