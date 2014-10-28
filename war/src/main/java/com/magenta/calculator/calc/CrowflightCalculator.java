package com.magenta.calculator.calc;

import com.magenta.calculator.cities.City;

import static java.lang.Math.*;

/**
 * Класс описывает метод вычисления расстояния между двумя городами
 * вдоль большого круга.
 */
class CrowflightCalculator implements Calculator {
	/* Радиус земли */
	private static final double Re=6378.136; //km
	/**
	 * Дистанция между двумя городами вдоль геодезических линий сферы.
	 * @param from исходный город;
	 * @param to конечный кород;
	 * @return дистанция в километрах.
	 */
	@Override
	public float calc(City from, City to) {
        final double DEG = Math.PI/180.;
		double c2Phi = cos(from.getLatitude()*DEG)*cos(to.getLatitude()*DEG);
		double s2Phi = sin(from.getLatitude()*DEG)*sin(to.getLatitude()*DEG);
		double cL1mL2 = cos((from.getLongitude()-to.getLongitude())*DEG);
		return (float) (Re*acos(c2Phi*cL1mL2+s2Phi));
	}
	/**
	 * Имя метода.
	 * @return имя.
	 */
	@Override
	public String getName() {
		return "Crowflight";
	}
}
