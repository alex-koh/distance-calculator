package com.magenta.calculator.data;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertNotNull;

abstract public class TestUtilDAO<T> extends TestDataSource {
	private PreparedStatement clean;
	PreparedStatement select;

	abstract public String getDeleteSQL();
	abstract public String getSelectSQL();
	abstract public Object getDAO();

	protected boolean compareFloat(float f1, float f2) {
		final double epsilon = 1e-6;
		double delta = f1-f2;
		double median = (f1+f2)/2;
		return Math.abs(delta)<epsilon*Math.abs(median);
	}

	abstract protected boolean compare(T a, T b);
	abstract protected List<T> getTT();
	abstract protected void insert(T t) throws Exception;
	abstract protected T newT (ResultSet result) throws Exception;
	abstract protected String toStringT (T t);
	abstract protected Integer getKey (T t);
	abstract protected T find (T t) throws Exception;

	@BeforeClass
	public void beforeUtilDAO() throws Exception{
		clean = testConnection.prepareStatement(getDeleteSQL());
		select = testConnection.prepareStatement(getSelectSQL());
	}

	@BeforeMethod
	public void beforeUtilDAOMethod() throws Exception{
		clean.execute();
	}
	@Test
	public void testCreateDao() throws Exception {
		assertNotNull(getDAO());
	}

	private String errorMessage(T exp, T act) {
		return "city must be equal " +
			"expected=[" + toStringT(exp) + "] " +
				"but actual=[" + toStringT(act)+"]";
	}

	@Test(dependsOnMethods = "testCreateDao", groups = "util")
	public void testInsert() throws Exception {
		List<T> tt = getTT();
		for(T t : tt)
			insert(t);
		ResultSet res = select.executeQuery();
		int i=0;
		while (res.next()) {
			T t = newT(res);
			T exp = tt.get(i++);
			assertTrue(compare(exp, t), errorMessage(exp, t));
		}
	}

	@Test(dependsOnMethods = "testInsert")
	public void testFind() throws Exception {
		Map<Integer,T> map = new HashMap<Integer, T>();
		for(T t : getTT()) {
			insert(t);
			map.put(getKey(t), t);
		}
		for (T t : map.values()) {
			T act = find(t);
			assertTrue(compare(t, act), errorMessage(t, act));
		}
	}

	@AfterMethod
	public void afterUtilDAOMethod() throws Exception{
		clean.execute();
	}

	@AfterClass
	public void afterUtilDAO() throws Exception{
		clean.close();
		select.close();
	}
}
