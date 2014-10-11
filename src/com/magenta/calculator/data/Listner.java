package com.magenta.calculator.data;

import com.magenta.calculator.cities.Cities;
import com.magenta.calculator.cities.City;
import com.magenta.calculator.cities.Distance;
import com.magenta.calculator.cities.Distances;

import javax.xml.bind.Unmarshaller;

/**
 * Обработчик событий JAXB-анализатора, возникающих при разборе XML-файла
 * при загрузке информации в БД.
 */
public class Listner extends Unmarshaller.Listener{
    private Saver saver;

	/**
	 * Конструктор сохраняет ссылку на объект, пишущий в БД
	 * @param saver объект-писатель
	 */
    public Listner(Saver saver) {
        this.saver = saver;
    }

	/**
	 * Событие возникает перед началом началом заполнения полей узла, но после
	 * создания самого объекта узла. Метод выполняет удаление всех предыдущих
	 * сохраненных узлов из результата.
	 * @param target текущий загружаемый узел.
	 * @param parent родительский узел.
	 */
    @Override
    public void beforeUnmarshal(Object target, Object parent) {
        super.beforeUnmarshal(target, parent);
		if (target instanceof City) {
			Cities cities = (Cities) parent;
			cities.getCity().clear();
		}
		if (target instanceof Distance) {
			Distances distances = (Distances) parent;
			distances.getDistance().clear();
		}
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
                saver.write((City) target);
            }
            if (target instanceof Distance) {
                saver.write((Distance) target);
            }
            if (target instanceof Cities) {
                saver.makeIndex();
				Cities cities = (Cities) target;
				cities.getCity().clear();
            }
			if (target instanceof Distances) {
				Distances distances = (Distances) target;
				distances.getDistance().clear();
			}
        }
        catch (Exception exc) {
            //TODO
        }
    }
}
