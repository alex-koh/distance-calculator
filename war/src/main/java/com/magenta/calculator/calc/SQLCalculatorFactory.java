package com.magenta.calculator.calc;

import com.magenta.calculator.data.DAOFactory;

import com.opensymphony.xwork2.inject.Inject;
import java.util.*;

/**
 Фабрика создающая вычислители расстояний
 */
public class SQLCalculatorFactory implements CalculatorFactory {
	private Map<String,Calculator> calculators;
	private DAOFactory factory;
	private List<String> names;
	/**
	 * Конструктор фабрики
	 */
	@Inject
	public SQLCalculatorFactory(DAOFactory factory) {
		this.factory = factory;
		calculators.put(CrowflightCalculator.class.getName(), new CrowflightCalculator());
		calculators.put(ZeroCalculator.class.getName(), new ZeroCalculator());
		calculators = Collections.unmodifiableMap(calculators);

		names = new ArrayList<String>(calculators.keySet());
		names.add(DistanceMatrix.class.getName());
		names = Collections.unmodifiableList(names);
	}

	/**
	 * Возвращает новый экземпляр вычислителя по его имени
	 * @param name имя вычислителя
	 * @return экземпляр вычислителя (возвращает null, если вычислитель с таким
	 * именем не найден)
	 */
	@Override
	public Calculator getCalculator(String name) {
		Calculator c = calculators.get(name);
		if (c != null) {
			return c;
		}
		else if (DistanceMatrix.class.getName().equals(name)) {
			try {
				return new DistanceMatrix(factory);
			}
			catch (Exception exc) { }
		}
		return null;
	}

	/**
	 * Возвращает список имен доступных вычислителей
	 * @return неизменяемый список имен
	 */
	@Override
	public Collection<String> getNames() {
		return names;
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
