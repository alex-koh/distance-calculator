package com.magenta.calculator.data;

/**
 * Created by alex on 10/29/14.
 */
public interface DAOFactory {
	public CityDAO getCityDao() throws Exception;
	public DistanceDAO getDistanceDao() throws Exception;
}
