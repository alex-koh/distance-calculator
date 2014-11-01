package com.magenta.calculator;

import com.magenta.calculator.data.DAOFactory;
import com.magenta.calculator.data.Listener;
import com.opensymphony.xwork2.Action;
import java.io.File;

import com.opensymphony.xwork2.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * Действие выполняет загрузку XML-файла с компьютера пользователя.
 */
public class AdminAction implements Action {
	@Inject
	private DAOFactory daoFactory;
	@Inject(value = "com.magenta.calculator.contextPath")
	private String contextPath;
	private File file;

	public AdminAction() {
	}

	@Override
	public String execute() throws Exception {

		JAXBContext context = JAXBContext.newInstance(contextPath);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setListener(new Listener(daoFactory));

		unmarshaller.unmarshal(file);
		return Action.SUCCESS;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
