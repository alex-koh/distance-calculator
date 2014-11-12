package com.magenta.calculator.calc;

import com.magenta.calculator.cities.City;
import com.opensymphony.xwork2.ActionContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;


@Test(groups = "calc")
public class TestCrowflight {
	private CalculatorFactory factory;
	private Calculator calculator;
	private final String name = CrowflightCalculator.class.getName();

	private Random rnd = new Random();

	/**
	 * Простая векторная арифметика
	 */
	private double mult(double[] a, double[] b) {
		return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
	}

	private double norm(double[] r) {
		return Math.sqrt(mult(r,r));
	}

	private double[] scale(double[] r, double s) {
		return new double[] {r[0]*s, r[1]*s, r[2]*s};
	}

	private double norm1(double[] r) {
		return Math.sqrt(r[0]*r[0] + r[1]*r[1]);
	}

	private double[] cross(double[] a, double[] b) {
		return new double[] {
			a[1]*b[2]-a[2]*b[1],
			a[2]*b[0]-a[0]*b[2],
			a[0]*b[1]-a[1]*b[0]
		};
	}
	private double[] sum(double[] a, double[] b) {
		return new double[] {
			a[0]+b[0],
			a[1]+b[1],
			a[2]+b[2]
		};
	}

	/**
	 * Функция устанавливает координаты города, соответствующие данному вектору
	 * @param r единичный вектор
	 * @return город с установленными координатами
	 */
	private City getCity(double[] r) {
		final double RAD = 180./Math.PI;
		double r1 = norm1(r);
		City city = new City();
		city.setLatitude(new Float(Math.atan2(r[2],r1)*RAD));
		city.setLongitude(new Float(Math.atan2(r[1],r[0])*RAD));
		return city;
	}

	/**
	 * Функция возвращает вектор, отстоящий от вектора r на угол a в направлении
	 * вектора b
	 * @param r исходный вектор
	 * @param b направление
	 * @param a угол между исходным и результирующим векторами
	 * @return результирующий вектор
	 */
	private double[] getR(double[] r, double [] b, double a) {
		return sum(scale(r, Math.cos(a)), scale(b, Math.sin(a)));
	}

	/**
	 * Фунция возвращает массив состоящий из двух городов с заполненными
	 * координатами, третьего горда, отстоящего от первого на угол a и собственно
	 * угла a.
	 * @return массив результатов
	 */
	private Object[] getPoints() {
		double[] r = {rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble()};
		r = scale(r, 1/norm(r));

		double[] b = {rnd.nextDouble(), rnd.nextDouble(), rnd.nextDouble()};
		b = cross(r, b);
		b = scale(b, 1/norm(b));

		double a = rnd.nextDouble()* Math.PI;

		return new Object[] {
			getCity(r),
			getCity(getR(r,b,a)),
			getCity(getR(r,b,1)),
			a
		};
	}

	/**
	 * Функция генерирует массив исходных данных для тестов
	 * @return массив исходных данных для тестов
	 */
	@DataProvider
	public Object[][] dataTest() {
		int size = 10;
		Object[][] result = new Object[10][];
		List<Object[]> poinst = new ArrayList<Object[]>(size);
		for(int i =0; i<size; i++) {
			poinst.add(getPoints());
		}
		return poinst.toArray(result);
	}

	/**
	 * Создает фабрику вычислителей
	 */
	@BeforeClass
	public void beforeCrowflight() throws Exception{
		factory =  new SQLCalculatorFactory(null);
	}

	/**
	 * создает вычислитель
	 */
	@BeforeMethod
	public void beforeCrowflightMethod() {
		calculator = factory.getCalculator(name);
	}

	/**
	 * Функция проверяет работу вычислителей
	 * @param from город отправления
	 * @param to город прибытия
	 * @param to1 угловое расстояние между городом from и данным городом
	 *               составляет 1 рад
	 * @param a угол между городами в радианах
	 * @throws Exception
	 */
	@Test(dataProvider = "dataTest")
	public void test(City from, City to, City to1, double a) throws Exception {
		final double epsilon = 1e-6;
		double d = calculator.calc(from, to);
		double r = calculator.calc(from, to1);
		assertEquals(d, r*a, (d+r*a)/2*epsilon, "Re="+r);
	}
}
