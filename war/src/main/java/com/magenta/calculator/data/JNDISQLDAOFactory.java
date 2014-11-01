package com.magenta.calculator.data;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.opensymphony.xwork2.inject.Inject;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by alex on 10/30/14.
 */
public class JNDISQLDAOFactory implements DAOFactory {
	private DataSource dataSource;
	private <T extends SQLDAOSettable>
	T getDAO(Class<T> clazz) throws SQLException {
		T dao;
		try {
			dao = clazz.newInstance();
		}
		catch (Exception exc) {
			return null;
		}
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			dao.setConnection(connection);
			connection = null;
			return dao;
		}
		finally {
			if (connection != null)
				connection.close();
		}
	}
	@Resource(name="java:comp/jdbc/com/magenta/calculator/DistanceCalculator")
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	@Override
	public CityDAO getCityDao() throws SQLException {
		return getDAO(SQLCityDAO.class);
	}

	@Override
	public DistanceDAO getDistanceDao() throws SQLException {
		return getDAO(SQLDistanceDAO.class);
	}
}
