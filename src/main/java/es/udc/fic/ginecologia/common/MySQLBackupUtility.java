package es.udc.fic.ginecologia.common;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.smattme.MysqlExportService;
import com.smattme.MysqlImportService;

public class MySQLBackupUtility {
	
	private MysqlExportService mysqlExportService;
	private MysqlImportService mysqlImportService;
	private String JDBCDriver;
	private String JDBCConnectionString;
	private String dbUsername;
	private String dbPassword;

	public MySQLBackupUtility() throws ClassNotFoundException, IOException, SQLException {
		this.mysqlExportService = generateMysqlExportService();
		this.mysqlImportService = generateMysqlImportService();
	}

	public MysqlExportService getMysqlExportService() {
		return mysqlExportService;
	}

	public MysqlImportService getMysqlImportService() {
		return mysqlImportService;
	}

	private MysqlExportService generateMysqlExportService() throws ClassNotFoundException, IOException, SQLException {
		
		Properties properties = new Properties();
		properties.setProperty(MysqlExportService.JDBC_DRIVER_NAME, "com.mysql.cj.jdbc.Driver");
		properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, "jdbc:mysql://localhost:3306/ginecology");
		properties.setProperty(MysqlExportService.DB_USERNAME, "root");
		properties.setProperty(MysqlExportService.DB_PASSWORD, "root");

		properties.setProperty(MysqlExportService.TEMP_DIR, new File("external").getPath());

		properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");
		properties.setProperty(MysqlExportService.PRESERVE_GENERATED_SQL_FILE, "true");

		MysqlExportService mysqlExportService = new MysqlExportService(properties);
		mysqlExportService.export();

		return mysqlExportService;
	}

	private MysqlImportService generateMysqlImportService() {
		MysqlImportService mysqlImportService = MysqlImportService.builder()
				.setJdbcConnString("jdbc:mysql://localhost:3306/ginecology")
				.setJdbcDriver("com.mysql.cj.jdbc.Driver")
				.setUsername("root")
				.setPassword("root")
				.setDeleteExisting(true)
				.setDropExisting(true);

		return mysqlImportService;
	}

}
