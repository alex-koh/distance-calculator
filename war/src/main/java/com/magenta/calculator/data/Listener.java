package com.magenta.calculator.data;

import com.magenta.calculator.cities.City;
import com.magenta.calculator.cities.Cities;
import com.magenta.calculator.cities.Distance;
import com.magenta.calculator.cities.Distances;

import javax.xml.bind.Unmarshaller;
import java.util.Map;

/**
 * Обработчик событий JAXB-анализатора, возникающих при разборе XML-файла
 * при загрузке информации в БД.
 */
public class Listener extends Unmarshaller.Listener{
    private CityDAO cityDAO;
	private DistanceDAO distanceDAO;
	/**
	 * Создает слушателя, обрабатывающего элементы XML-файла
	 * @param factory фабрика объектов доступа к БД
	 */
	public Listener(DAOFactory factory) throws Exception{
		this.cityDAO = factory.getCityDao();
		this.distanceDAO = factory.getDistanceDao();
	}

	/**
	 * Событие возникает после заполнения всех полей узла. Обработчик
	 * записывает полученный объект в базу данных или рассчитывает отображение
	 * индексов XML-файла, на индексы в БД.
	 * @param target текущий загружаемый узел.
	 * @param parent родительский узел.
	 */
    @Override
    public void afterUnmarshal(Object target, Object parent) {
        super.afterUnmarshal(target, parent);
        try {
            if (target instanceof City) {
				City city = (City) target;
				((Cities) parent).getCity().clear();
				cityDAO.insert(city);
            }
			if (target instanceof Cities) {
				((Cities) target).getCity().clear();
			}
            if (target instanceof Distance) {
				Distance distance = (Distance) target;
				((Distances) parent).getDistance().clear();
				distanceDAO.insert(distance);
            }
			if (target instanceof Distances) {
				((Distances) target).getDistance().clear();
			}
        }
        catch (Exception exc) {
            exc.printStackTrace(); //tODO
        }
    }
}
