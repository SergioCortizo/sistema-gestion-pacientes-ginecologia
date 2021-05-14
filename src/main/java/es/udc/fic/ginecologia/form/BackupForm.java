package es.udc.fic.ginecologia.form;

import org.springframework.web.multipart.MultipartFile;

public class BackupForm {

	private MultipartFile backupFile;

	public BackupForm() {
		
	}

	public MultipartFile getBackupFile() {
		return backupFile;
	}

	public void setBackupFile(MultipartFile backupFile) {
		this.backupFile = backupFile;
	}

	@Override
	public String toString() {
		return backupFile.getName();
	}
	
}
