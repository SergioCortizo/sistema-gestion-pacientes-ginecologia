package es.udc.fic.ginecologia.common;

import java.util.Comparator;

import es.udc.fic.ginecologia.model.GrupalMessage;

public class GrupalMessageByDateDescendingComparator implements Comparator<GrupalMessage> {

	@Override
	public int compare(GrupalMessage arg0, GrupalMessage arg1) {
		if (arg0.getDatetime().isBefore(arg1.getDatetime())) return 1;
		else return -1;
	}

}
