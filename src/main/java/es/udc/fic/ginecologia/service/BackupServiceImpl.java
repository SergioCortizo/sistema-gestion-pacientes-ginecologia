package es.udc.fic.ginecologia.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.smattme.MysqlExportService;
import com.smattme.MysqlImportService;

@Service
@Transactional
public class BackupServiceImpl implements BackupService{
	
	@Value("${spring.datasource.driver-class-name}")
	private String JDBCDriver;
	
	@Value("${SPRING_DATASOURCE_URL:${spring.datasource.url}}")
	private String JDBCConnectionString;
	
	@Value("${SPRING_DATASOURCE_USERNAME:${spring.datasource.username}}")
	private String dbUsername;
	
	@Value("${SPRING_DATASOURCE_PASSWORD:${spring.datasource.password}}")
	private String dbPassword;

	@Override
	public File generateBackup() throws ClassNotFoundException, IOException, SQLException {
		Properties properties = new Properties();
		properties.setProperty(MysqlExportService.JDBC_DRIVER_NAME, JDBCDriver);
		properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, JDBCConnectionString);
		properties.setProperty(MysqlExportService.DB_USERNAME, dbUsername);
		properties.setProperty(MysqlExportService.DB_PASSWORD, dbPassword);
		
		File dir = new File("external");

		properties.setProperty(MysqlExportService.TEMP_DIR, dir.getPath());

		properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");
		properties.setProperty(MysqlExportService.PRESERVE_GENERATED_SQL_FILE, "false");

		MysqlExportService mysqlExportService = new MysqlExportService(properties);
		mysqlExportService.export();
		
		File file = mysqlExportService.getGeneratedZipFile();
		
		mysqlExportService.clearTempFiles();
		
		return file;
	}

	@Override
	public boolean restoreDatabase(MultipartFile file) throws IOException, ClassNotFoundException, SQLException {
		MysqlImportService mysqlImportService = MysqlImportService.builder()
				.setJdbcConnString(JDBCConnectionString)
				.setJdbcDriver(JDBCDriver)
				.setUsername(dbUsername)
				.setPassword(dbPassword)
				.setDeleteExisting(true)
				.setDropExisting(false);
		
		String content = new String(file.getBytes()).replace("INSERT", "REPLACE");
		mysqlImportService.setSqlString(content);
		
		return mysqlImportService.importDatabase();
	}
	
	

}
