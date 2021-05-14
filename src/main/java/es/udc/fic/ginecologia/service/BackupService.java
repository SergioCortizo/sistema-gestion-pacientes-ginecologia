package es.udc.fic.ginecologia.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.springframework.web.multipart.MultipartFile;

public interface BackupService {

	public File generateBackup() throws ClassNotFoundException, IOException, SQLException;
	
	public boolean restoreDatabase(MultipartFile file) throws IOException, ClassNotFoundException, SQLException;
}
