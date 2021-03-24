package es.udc.fic.ginecologia.common;

import java.util.Comparator;

import es.udc.fic.ginecologia.model.Meeting;

public class MeetingByDateDescendingComparator implements Comparator<Meeting> {

	@Override
	public int compare(Meeting arg0, Meeting arg1) {
		if (arg0.getMeeting_date().isBefore(arg1.getMeeting_date())) return 1;
		else return -1;
	}

}
