package es.udc.fic.ginecologia.form;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import es.udc.fic.ginecologia.model.CommonTask;

public class CommonTaskConversor {

	public static final List<CommonTaskElemList> convertoToCommonTaskElemList(Iterable<CommonTask> commonTasks,
			Integer userId) {

		List<CommonTaskElemList> result = new ArrayList<>();

		commonTasks.forEach(ct -> {
			CommonTaskElemList element = new CommonTaskElemList();

			element.setId(ct.getId());
			element.setTitle(ct.getTitle());

			LocalDateTime lastTimeRead = StreamSupport.stream(ct.getCommonTaskUsers().spliterator(), false)
					.filter(ctm -> ctm.getUser().getId() == userId).findFirst().get().getLast_time_read();

			element.setLastTimeRead(lastTimeRead);

			element.setNew(ct.getMessages().isEmpty());

			if (!element.isNew()) {

				long messages = StreamSupport.stream(ct.getMessages().spliterator(), false)
						.filter(m -> m.getDatetime().isAfter(lastTimeRead)).count();

				element.setNewMessages(messages);

			}

			result.add(element);
		});

		result.sort((ct1, ct2) -> {
			
			if (ct1.isNew()) return -1;

			if (ct1.getNewMessages() > 0 || ct2.getNewMessages() > 0) {
				if (ct1.getNewMessages() == ct2.getNewMessages()) {
					return 0;
				}

				return ct1.getNewMessages() > 0 ? -1 : 1;
			}

			return ct1.getLastTimeRead().isAfter(ct2.getLastTimeRead()) ? -1 : 1;
		});

		return result;
	}
}
