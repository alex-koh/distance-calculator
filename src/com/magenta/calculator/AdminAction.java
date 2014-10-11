package com.magenta.calculator;

import com.magenta.calculator.calc.CalculatorFactory;
import com.magenta.calculator.calc.SQLCalculatorFactory;
import com.magenta.calculator.data.Listner;
import com.magenta.calculator.data.Loader;
import com.magenta.calculator.data.SQLSaver;
import com.magenta.calculator.data.Saver;
import com.opensymphony.xwork2.Action;
import java.io.File;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * Действие выполняемое при загрузке XML-файла на странице администратора.
 */
public class AdminAction implements Action {
	private Loader loader;
	private CalculatorFactory calcFactory;
	private File file;
	private String contextPath;
	/**
	 * Метод выполняет загрузку файла, полученного от клиента и устанавливает
	 * начальные сосояние всего приложения. Отвечает за инициализацию источника
	 * и фабрики вычислителей.
	 * @return возвращает SUCCESS, если все операции выполнены успешно.
	 * @throws Exception возвращает ошибку в случае её возникновения
	 */
	@Override
	public String execute() throws Exception {
		PoolProperties properties = new PoolProperties();

		DataSource dataSource = new DataSource();
		dataSource.setPoolProperties(properties);

		Saver saver = new SQLSaver(dataSource);

		JAXBContext context = JAXBContext.newInstance(contextPath);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setListener(new Listner(saver));

		unmarshaller.unmarshal(file);

		loader = saver.getLoader();

		// Згрузка списка вычислителей
		calcFactory = new SQLCalculatorFactory(loader);

		return Action.SUCCESS;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public Loader getLoader() {
		return loader;
	}

	public CalculatorFactory getCalcFactory() {
		return calcFactory;
	}
}
