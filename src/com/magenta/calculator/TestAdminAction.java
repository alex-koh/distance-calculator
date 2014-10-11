/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.magenta.calculator;

import com.magenta.calculator.calc.CalculatorFactory;
import com.magenta.calculator.calc.SQLCalculatorFactory;
import com.opensymphony.xwork2.Action;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.io.File;
import java.sql.*;
import java.util.*;

import static org.mockito.Mockito.*;

/**
 *
 * @author alex
 */
public class TestAdminAction implements Action {
	private DataSource dataSource;
	private List<Map<String,Object>> cities;
	private CalculatorFactory calcFactory;
	private File file;
	/**
	 * Метод выполняет загрузку файла от клиента и устанавливает начальные 
	 * сосояние всего приложения. Отвечает за инициализацию источника данных,
	 * за создание списка городов и списка вычислителей.
	 * @return возвращает SUCCESS, если все операции выполнены успешно.
	 * @throws Exception возвращает ошибку в случае её возникновения
	 */
	@Override
	public String execute() throws Exception {
		PoolProperties properties = new PoolProperties();
		/* //TODO Fake
        dataSource = new DataSource();
		dataSource.setPoolProperties(properties);
		*/ //TODO Fake

        dataSource = mock(DataSource.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(eq(1))).thenReturn("Samara");
        when(resultSet.getFloat(eq(2))).thenReturn(53.5f);
        when(resultSet.getFloat(eq(3))).thenReturn(51.4f);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(dataSource.getConnection()).thenReturn(connection);
		/* //TODO Fake
		Connection connection = dataSource.getConnection();
		try {
			connection.setAutoCommit(true);
			// Загрузка списка городов
			initCities(connection);
		}
		finally {
			connection.close();
		}
		*/

		// Згрузка списка вычислителей
		calcFactory = SQLCalculatorFactory.getInstance();

		cities = new ArrayList<Map<String, Object>>();
		int i=1;
		for(String s : new String[] {
			"Samara", "Pohvisnevo",	"Sizran", "Tolyatty", "Buzuluc", "N-sk", "Chapaevsk"}) {
            Map<String,Object> c = new HashMap<String, Object>();
            c.put("name",s);
            c.put("id", i++);
            cities.add(Collections.unmodifiableMap(c));
		}
        cities = Collections.unmodifiableList(cities);

		return Action.SUCCESS;
	}

    public String initDataBase() throws Exception {

        return Action.SUCCESS;
    }

	private void initCities(Connection connection) throws SQLException{
		cities = new ArrayList<Map<String, Object>>();
		Statement st = connection.createStatement();
		ResultSet result = st.executeQuery("SELECT id,name FROM City");
		while (result.next()) {
            Map<String,Object> c = new HashMap<String, Object>();
            c.put("id",result.getInt(1));
            c.put("name", result.getString(2));
            cities.add(Collections.unmodifiableMap(c));
        }
        cities = Collections.unmodifiableList(cities);
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Map<String, Object>> getCities() {
        return cities;
    }

    public void setCities(List<Map<String, Object>> cities) {
        this.cities = cities;
    }

    public CalculatorFactory getCalcFactory() {
        return calcFactory;
    }

    public void setCalcFactory(CalculatorFactory calcFactory) {
        this.calcFactory = calcFactory;
    }
}
