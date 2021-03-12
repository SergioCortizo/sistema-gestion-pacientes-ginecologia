package es.udc.fic.ginecologia.form;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import es.udc.fic.ginecologia.model.Speciality;

public class SpecialityConversor {
	public static List<SpecialityLine> convertToSpecialityLine(Iterable<Speciality> specialities) {

		List<SpecialityLine> result = StreamSupport.stream(specialities.spliterator(), false)
				.map(s -> new SpecialityLine(s.getId(), s.getName(), s.isEnabled())).collect(Collectors.toList());

		return result;
	}
}
