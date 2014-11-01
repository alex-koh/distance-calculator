package com.magenta.calculator.data;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.testng.annotations.*;

import static org.testng.Assert.*;

import com.magenta.calculator.cities.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@Test(singleThreaded = true)
public class TestCityDAO {
	private DAOFactory factory;
	private CityDAO dao;
	private PreparedStatement cleanCity;
	private PreparedStatement selectCities;
	@BeforeClass
	public void beforeClass() throws Exception{
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
		JNDISQLDAOFactory factory = new JNDISQLDAOFactory();
		factory.setDataSource(dataSource);
		this.factory = factory;

		Connection testConnection = dataSource.getConnection("calculator.test", "test");
		cleanCity = testConnection.prepareStatement("delete from City;");
		selectCities = testConnection.prepareStatement("select id_key,Name,Latitude,Longitude from  City;");
	}

	@BeforeMethod
	public void beforeMethod() throws Exception{
		this.dao = factory.getCityDao();
		cleanCity.execute();
	}

	@Test(enabled = true)
	public void testCreate() {
		assertNotNull(dao);
	}

	public boolean equalCity(City c1, City c2) {
		final double epsilon = 1e-6;
		double delta1 = c1.getLatitude()-c2.getLatitude();
		double delta2 = c1.getLongitude()-c2.getLongitude();
		double median1 = (c1.getLatitude()+c2.getLatitude())/2;
		double median2 = (c1.getLongitude()+c2.getLongitude())/2;

		return c1.getKey()==c2.getKey()
				&& c1.getName().equals(c2.getName())
				&& Math.abs(delta1)<epsilon*Math.abs(median1)
				&& Math.abs(delta2)<epsilon*Math.abs(median2);
	}

	private void setCity(City city, int key, String name, float la, float lo) {
		city.setKey(key);
		city.setName(name);
		city.setLatitude(la);
		city.setLongitude(lo);
	}

	private String toStringCity(City city) {
		return city.getKey()+" "+city.getName()+" "+
			city.getLatitude()+ " "+ city.getLongitude();
	}

	private List<City> getCities() throws Exception{
		int[]  keys  = {1,2,3,4,3,2,3,5,6};
		String name = "Samara";
		float lo = 4.5f;
		float la = 5.4f;
		List<City> cities = new ArrayList<City>();
		int i = 0;
		for (int key : keys) {
			City city = new City();
			setCity(city, key, name+Integer.toString(key+10*i++),
					lo+9.34f*key, la-8.2f*key);
			cities.add(city);
		}
		return cities;
	}

	@Test(dependsOnMethods = "testCreate")
	public void testInsert() throws Exception {
		List<City> cities = getCities();
		for(City c : cities)
			dao.insert(c);
		ResultSet res = selectCities.executeQuery();
		int i=0;
		while (res.next()) {
			City city = new City();
			setCity(city, res.getInt(1), res.getString(2), res.getFloat(3), res.getFloat(4));
			City exp = cities.get(i++);
			assertTrue(equalCity(exp, city),
				"city must be equal " +
				"expected=[" + toStringCity(exp) + "] " +
				"but actual=[" + toStringCity(city)+"]"
			);
		}
	}

	@Test(dependsOnMethods = "testInsert")
	public void testFind() throws Exception {
		Map<Integer,City> map = new HashMap<Integer, City>();
		for(City c : getCities()) {
			dao.insert(c);
			map.put(c.getKey(),c);
		}
		for (City c : map.values()) {
			City act = dao.find(c.getKey());
			assertTrue(equalCity(c, act),
					"city must be equal " +
					"expected=[" + toStringCity(c) + "] " +
					"but actual=[" + toStringCity(act)+"]"
			);
		}
	}

	@Test(dependsOnMethods = "testInsert")
	public void testSelectNames() throws Exception {
		Map<Integer,City> map = new HashMap<Integer, City>();
		for(City c : getCities()) {
			dao.insert(c);
			map.put(c.getKey(),c);
		}
		Iterator<Map<String,Object>> p = dao.selectNames().iterator();
		for (City c : map.values()) {
			assertTrue(p.hasNext());
			Object[] a = p.next().values().toArray();
			assertEquals(a.length,2);
			long hash = Arrays.deepHashCode(a);
			long hash1 = Arrays.deepHashCode(new Object[]{c.getKey(), c.getName()});
			long hash2 = Arrays.deepHashCode(new Object[]{c.getName(), c.getKey()});
			assertTrue((hash==hash1)||(hash==hash2));
		}
	}

	@AfterMethod
	public void afterMethod() throws Exception{
		cleanCity.execute();
	}

	@AfterClass
	public void afterClass() throws Exception{
		cleanCity.getConnection().close();
	}
}
