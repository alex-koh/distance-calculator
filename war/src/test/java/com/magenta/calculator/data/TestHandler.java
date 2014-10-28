package data;

import com.magenta.calculator.cities.City;
import com.magenta.calculator.cities.Distance;
import com.magenta.calculator.cities.Map;
import com.magenta.calculator.data.Listner;
import com.magenta.calculator.data.Saver;
import org.mockito.InOrder;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.net.URL;

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by alex on 10/2/14.
 */
public class TestHandler {
    private Saver saver;
    private Map map;
	private Unmarshaller unmarshaller;
	private URL resource;

    @BeforeTest
    public void init() throws Exception {
		/*
        saver = mock(Saver.class);

		resource = getClass().getResource("test_map.xml");

        JAXBContext context = JAXBContext.newInstance(
				City.class.getPackage().getName());
        unmarshaller = context.createUnmarshaller();

        map = (Map) unmarshaller.unmarshal(
				new InputSource(resource.openStream()));

        unmarshaller.setListener(new Listner(saver));
        */
    }

    @Test(enabled = false)
    public void test() throws Exception {
		InOrder order = inOrder(saver);

		Map newMap = (Map) unmarshaller.unmarshal(
				new InputSource(resource.openStream()));

		order.verify(saver, times(map.getCities().getCity().size()))
				.write(any(City.class));
		order.verify(saver).makeIndex();
		order.verify(saver, times(map.getDistances().getDistance().size()))
				.write(any(Distance.class));

		assertEquals(newMap.getCities().getCity().size(),0,"must be zero");
		assertEquals(newMap.getDistances().getDistance().size(),0,"must be zero");
    }
}