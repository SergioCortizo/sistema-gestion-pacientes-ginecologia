package es.udc.fic.ginecologia.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import es.udc.fic.ginecologia.common.exception.InstanceNotFoundException;
import es.udc.fic.ginecologia.common.exception.PermissionException;
import es.udc.fic.ginecologia.model.Answer;

public interface MeetingService {

	void addMeeting(Integer userId, Long patientId, String activity, String comments, List<Answer> answers,
			List<Integer> diagnosticTestIds, List<MultipartFile> files)
			throws InstanceNotFoundException, PermissionException;

}
