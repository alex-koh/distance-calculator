package com.magenta.calculator.data;

import com.magenta.calculator.cities.Distance;
import org.testng.annotations.BeforeMethod;

import java.sql.ResultSet;
import java.util.*;

/**
 * Created by alex on 11/2/14.
 */
public class TestDistanceDAO extends TestUtilDAO<Distance> {
	private DistanceDAO dao;

	public String getDeleteSQL() {
		return "delete from Distance;";
	}
	public String getSelectSQL() {
		return "select fromCity,toCity,Distance from  Distance;";
	}
	public Object getDAO() {
		return dao;
	}

	@Override
	protected boolean compare(Distance a, Distance b) {
		return a.getFrom()==b.getFrom()
				&& a.getTo()==b.getTo()
				&& compareFloat(a.getDistance(), b.getDistance());
	}

	@Override
	protected List<Distance> getTT() {
		List<Distance> distances = new ArrayList<Distance>();
		int size = 10;
		int j=0;
		for (int i=0, c1=3, c2=7; i<2.5*size; i++, c1=(c1+7)%size, c2=(c2+7)%size) {
			Distance distance = new Distance();
			setDistance(distance, c1, c2, (c1*10.f+c2+5*j++)/100.f);
			distances.add(distance);
		}
		return distances;
	}

	@Override
	protected void insert(Distance distance) throws Exception {
		dao.insert(distance);
	}

	@Override
	protected Distance newT(ResultSet result) throws Exception {
		Distance distance = new Distance();
		setDistance(distance, result.getInt(1), result.getInt(2), result.getFloat(3));
		return distance;
	}

	@Override
	protected String toStringT(Distance distance) {
		return distance.getFrom()+" "+distance.getTo()+" d="+distance.getDistance();
	}

	@Override
	protected Integer getKey(Distance distance) {
		return Arrays.deepHashCode(new Object[]{distance.getFrom(), distance.getTo()});
	}

	@Override
	protected Distance find(Distance distance) throws Exception {
		Distance d = new Distance();
		setDistance(d, distance.getFrom(), distance.getTo(), dao.find(distance.getFrom(), distance.getTo()));
		return d;
	}

	@BeforeMethod
	public void beforeDistanceDAOMethod() throws Exception{
		dao = factory.getDistanceDao();
	}

	private void setDistance(Distance d, int c1, int c2, float v) {
		d.setFrom(c1);
		d.setTo(c2);
		d.setDistance(v);
	}
}
