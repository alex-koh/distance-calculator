package com.magenta.calculator.data;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;

@Test(groups = "dao", singleThreaded = true)
public class TestDataSource {
	private DataSource dataSource;
	DAOFactory factory;
	Connection testConnection;
	@BeforeClass
	public void beforeDataSource() throws Exception{
		PoolProperties p = new PoolProperties();
		p.setUrl("jdbc:mysql://localhost:3306/TestDistanceCalculator");
		p.setDriverClassName("com.mysql.jdbc.Driver");
		p.setUsername("calculator.admin");
		p.setPassword("admin");
		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(100);
		p.setInitialSize(10);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMinIdle(10);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors(
				"org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
						"org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		p.setAlternateUsernameAllowed(true);


		DataSource dataSource = new DataSource();
		dataSource.setPoolProperties(p);
		this.dataSource = dataSource;

		JNDISQLDAOFactory factory = new JNDISQLDAOFactory(dataSource);

		this.factory = factory;

		testConnection = dataSource.getConnection("calculator.test", "test");
	}

	@AfterClass
	public void afterDataSource() throws Exception {
		testConnection.close();
		dataSource.close();
	}

}
