package com.magenta.calculator.data;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by alex on 10/30/14.
 */
public interface SQLDAOSettable {
	void setConnection(Connection connection) throws SQLException;
}
