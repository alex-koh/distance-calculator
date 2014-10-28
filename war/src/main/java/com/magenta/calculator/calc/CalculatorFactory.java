package com.magenta.calculator.calc;

import java.util.Collection;

/**
 * Created by alex on 10/10/14.
 */
public interface CalculatorFactory {
	Calculator getCalculator(String name);

	Collection<String> getNames();

	String getDescription(String name);
}
