package es.udc.fic.ginecologia.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MySQLBackupUtilityDefaultProperties {

	@Value("${spring.datasource.driver-class-name}")
	private String JDBCDriver;
	
	@Value("${spring.datasource.url}")
	private String JDBCConnectionString;
	
	@Value("${spring.datasource.username}")
	private String dbUsername;
	
	@Value("${spring.datasource.password}")
	private String dbPassword;

	public String getJDBCDriver() {
		return JDBCDriver;
	}

	public void setJDBCDriver(String jDBCDriver) {
		JDBCDriver = jDBCDriver;
	}

	public String getJDBCConnectionString() {
		return JDBCConnectionString;
	}

	public void setJDBCConnectionString(String jDBCConnectionString) {
		JDBCConnectionString = jDBCConnectionString;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
}
