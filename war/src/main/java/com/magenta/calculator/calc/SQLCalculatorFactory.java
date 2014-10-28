package com.magenta.calculator.calc;

import com.magenta.calculator.data.Loader;

import java.util.*;

/**
 Фабрика создающая вычислители расстояний
 */
public class SQLCalculatorFactory implements CalculatorFactory {
	private Map<String,Calculator> calculators;
	private Loader loader;

	/**
	 * Конструктор фабрики
	 */
	public SQLCalculatorFactory(Loader loader) {
		calculators = new HashMap<String, Calculator>();
		Calculator c = new CrowflightCalculator();
		calculators.put(c.getName(), c);
	}

	/**
	 * Возвращает новый экземпляр вычислителя по его имени
	 * @param name имя вычислителя
	 * @return экземпляр вычислителя (возвращает null, если вычислитель с таким
	 * именем не найден)
	 */
	@Override
	public Calculator getCalculator(String name) {
		return calculators.get(name);
	}

	/**
	 * Возвращает список имен доступных вычислителей
	 * @return неизменяемый список имен
	 */
	@Override
	public Collection<String> getNames() {
		return calculators.keySet();
	}

	/**
	 * Понятное для человека имя вычислителя
	 * @param name имя вычислителя
	 * @return имя, возвращаемое пользователю
	 */
    @Override
	public String getDescription(String name) {
        return "description";
    }
}
