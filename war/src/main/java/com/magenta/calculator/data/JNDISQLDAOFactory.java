package com.magenta.calculator.data;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by alex on 10/30/14.
 */
public class JNDISQLDAOFactory implements DAOFactory {
	private DataSource dataSource;
	public JNDISQLDAOFactory() throws Exception {
		java.util.logging.Logger log = java.util.logging.Logger.getLogger(getClass().getName());
		log.info("============ load ...");
		Context context = new InitialContext();
		Object obj = context.lookup("java:comp/env/jdbc/DistanceCalculator");
		if (obj instanceof DataSource)
			dataSource = (DataSource) obj;
		else
			throw new NullPointerException("dataSource==null");
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		}
		finally {
			if (connection != null)
				connection.close();
		}
	}
	public JNDISQLDAOFactory(DataSource dataSource) {
		this.dataSource = dataSource;
	}
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
	@Override
	public CityDAO getCityDao() throws SQLException {
		return getDAO(SQLCityDAO.class);
	}

	@Override
	public DistanceDAO getDistanceDao() throws SQLException {
		return getDAO(SQLDistanceDAO.class);
	}
}
