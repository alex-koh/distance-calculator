package com.magenta.calculator;

import com.magenta.calculator.calc.Calculator;
import com.magenta.calculator.calc.CalculatorFactory;
import com.magenta.calculator.cities.City;
import com.magenta.calculator.data.CityLoader;
import com.opensymphony.xwork2.Action;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 9/5/14.
 */
public class ResultAction implements  Action{
    private Map<String, Object> result;
    private CalculatorFactory calcFactory;
    private DataSource dataSource;
    private List<String> names = new ArrayList<String>();
    private Integer from;
    private Integer to;
    @Override
    public String execute() throws Exception {
        result = new HashMap<String, Object>();
        CityLoader loader = new CityLoader(dataSource);
        City fromCity = loader.load(from);
        City toCity = loader.load(to);

        List<Object> records = new ArrayList<Object>();
        for (String n : names) {
            Calculator c = calcFactory.getCalculator(n);
            if (c != null) {
                Map<String,Object> record = new HashMap<String,Object>();
                record.put("name", c.getName());
                record.put("value", c.calc(fromCity, toCity));
                records.add(record);
            }
        }
        result.put("result", records);
        return Action.SUCCESS;
    }

    public CalculatorFactory getCalcFactory() {
        return calcFactory;
    }

    public void setCalcFactory(CalculatorFactory calcFactory) {
        this.calcFactory = calcFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Map<String, Object> getResult() {
        return result;
    }
}
