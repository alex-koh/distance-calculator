package com.magenta.calculator.data;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import com.magenta.calculator.cities.City;

import java.sql.ResultSet;
import java.util.*;

public class TestCityDAO extends TestUtilDAO<City> {
	private CityDAO dao;

	@Override
	public String getDeleteSQL() {
		return "delete from City;";
	}

	@Override
	public String getSelectSQL() {
		return "select id_key,Name,Latitude,Longitude from  City;";
	}

	@Override
	public Object getDAO() {
		return dao;
	}


	@Override
	protected boolean compare(City a, City b) {
		return a.getKey()==b.getKey()
			&& a.getName().equals(b.getName())
			&& compareFloat(a.getLatitude(), b.getLatitude())
			&& compareFloat(a.getLongitude(), b.getLongitude());
	}

	@Override
	protected List<City> getTT() {
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

	@Override
	protected void insert(City city) throws Exception {
		dao.insert(city);
	}

	private void setCity(City city, int key, String name, float la, float lo) {
		city.setKey(key);
		city.setName(name);
		city.setLatitude(la);
		city.setLongitude(lo);
	}

	@Override
	protected City newT(ResultSet result) throws Exception {
		City city = new City();
		setCity(city, result.getInt(1), result.getString(2), result.getFloat(3), result.getFloat(4));
		return city;
	}

	@Override
	protected String toStringT(City city) {
		return city.getKey()+" "+city.getName()+" "+
				city.getLatitude()+ " "+ city.getLongitude();
	}

	@Override
	protected Integer getKey(City city) {
		return city.getKey();
	}

	@Override
	protected City find(City city) throws Exception {
		return dao.find(city.getKey());
	}

	@BeforeMethod
	public void beforeCityDAOMethod() throws Exception{
		this.dao = factory.getCityDao();
	}


	@Test(dependsOnGroups = "util")
	public void testSelectNames() throws Exception {
		Map<Integer,City> map = new HashMap<Integer, City>();
		for(City c : getTT()) {
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
}
