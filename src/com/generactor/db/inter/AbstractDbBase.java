package com.generactor.db.inter;

import java.sql.Connection;

public abstract class AbstractDbBase {
	
	public abstract Connection getDBConnection();
	
}
