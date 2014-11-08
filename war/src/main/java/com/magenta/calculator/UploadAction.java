package com.magenta.calculator;

import com.magenta.calculator.cities.City;
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
public class UploadAction implements Action {
	private DAOFactory daoFactory;
	private File mapOfCities;
	private String mapOfCitiesContentType;
	private String mapOfCitiesFileName;

	@Override
	public String execute() throws Exception {
		JAXBContext context =
				JAXBContext.newInstance(City.class.getPackage().getName());
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setListener(new Listener(daoFactory));
		unmarshaller.unmarshal(mapOfCities);
		return Action.SUCCESS;
	}
	@Inject
	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setMapOfCities(File mapOfCities) {
		this.mapOfCities = mapOfCities;
	}

	public void setMapOfCitiesContentType(String mapOfCitiesContentType) {
		this.mapOfCitiesContentType = mapOfCitiesContentType;
	}

	public void setMapOfCitiesFileName(String mapOfCitiesFileName) {
		this.mapOfCitiesFileName = mapOfCitiesFileName;
	}
}
