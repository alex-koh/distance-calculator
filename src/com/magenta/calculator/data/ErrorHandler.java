package com.magenta.calculator.data;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 10/5/14
 * Time: 7:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ErrorHandler implements ValidationEventHandler {

	@Override
	public boolean handleEvent(ValidationEvent event) {
		return false;
	}
}
