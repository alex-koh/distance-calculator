package com.magenta.calculator.data;

import com.magenta.calculator.cities.City;
import com.magenta.calculator.cities.Cities;
import com.magenta.calculator.cities.Distance;
import com.magenta.calculator.cities.Distances;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.xml.bind.annotation.XmlSchema;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.parsers.DocumentBuilderFactory;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.util.*;

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by alex on 10/2/14.
 */

@Test(groups = "xml")
public class TestListener {
	protected JAXBContext context;

	private Unmarshaller unmarshaller;
	private Listener listener;
	private DAOFactory factory;
	private CityDAO cityDAO;
	private DistanceDAO distanceDAO;
	private Document document;
	private String ns;
	private ValidationEventCollector handler;
	private final int sizes[] = {0,0};

    @BeforeMethod
    public void beforeListnerMethod() throws Exception {
		Class clazz = City.class;
		context = JAXBContext.newInstance(clazz.getPackage().getName());

		ns = clazz.getPackage().getAnnotation(XmlSchema.class).namespace();
		unmarshaller = context.createUnmarshaller();

		cityDAO = mock(CityDAO.class);
		distanceDAO = mock(DistanceDAO.class);

		factory = mock(DAOFactory.class);
		when(factory.getCityDao()).thenReturn(cityDAO);
		when(factory.getDistanceDao()).thenReturn(distanceDAO);

		listener = new Listener(factory);

		sizes[0] = sizes[1] = 0;
		unmarshaller.setListener(createWrapper(listener));
		handler = new ValidationEventCollector();
		unmarshaller.setEventHandler(handler);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		document = docFactory.newDocumentBuilder().newDocument();
    }

	Unmarshaller.Listener createWrapper(final Unmarshaller.Listener base) {
		return new Unmarshaller.Listener() {
			private void setSize(int size, int count) {
				if (size>sizes[count])
					sizes[count] = size;
			}
			private void setSize(Object obj) {
				if (obj instanceof Cities) {
					setSize(((Cities) obj).getCity().size(), 0);
				}
				if (obj instanceof Distances) {
					setSize(((Distances) obj).getDistance().size(), 1);
				}
			}
			@Override
			public void beforeUnmarshal(Object target, Object parent) {
				base.beforeUnmarshal(target,parent);
				setSize(parent);
				setSize(target);
			}

			@Override
			public void afterUnmarshal(Object target, Object parent) {
				//System.out.println(target.getClass().getName());
				base.afterUnmarshal(target, parent);
				setSize(parent);
				setSize(target);
			}
		};
	}

	Element createNode(Map<String,String> m, String name) {
		Element result = document.createElementNS(ns,name);
		for (Map.Entry<String,String> e : m.entrySet()) {
			Element node = document.createElementNS(ns, e.getKey());
			node.appendChild(document.createTextNode(e.getValue()));
			result.appendChild(node);
		}
		return result;
	}

    @Test(enabled = true)
    public void testCity() throws Exception {
		final int size = 5;

		Element cities = document.createElementNS(ns,"cities");
		for (int i=0; i<size; i++) {
			Map<String,String> m = new HashMap<String, String>();
			m.put("key",Integer.toString(i));
			m.put("name", "name");
			m.put("latitude","4.3");
			m.put("longitude", "-34.2");
			cities.appendChild(createNode(m, "city"));
		}
		Element root = document.createElementNS(ns,"map");
		root.appendChild(cities);

		unmarshaller.unmarshal(root);

		assertEquals(handler.getEvents().length,0,"some error(s) in xml-file");
		verify(cityDAO, times(size)).insert(any(City.class));
		assertEquals(sizes,new Integer[] {1, 0}, "too many records in jaxb-result objects");
    }

	@Test(enabled = true)
	public void testDistance() throws Exception {
		final int size = 3;

		Element distances = document.createElementNS(ns,"distances");
		for (int i=0; i<size; i++) {
			Map<String,String> m = new HashMap<String, String>();
			m.put("from",Integer.toString(i));
			m.put("to",Integer.toString(i+3));
			m.put("distance", "-34.2");
			distances.appendChild(createNode(m, "distance"));
		}
		Element root = document.createElementNS(ns,"map");
		root.appendChild(distances);

		unmarshaller.unmarshal(root);

		assertEquals(handler.getEvents().length, 0, "some error(s) in xml-file");
		verify(factory).getDistanceDao();
		verify(distanceDAO, times(size)).insert(any(Distance.class));
		assertEquals(sizes,new Integer[] {0, 1}, "too many records in jaxb-result objects");
	}
}