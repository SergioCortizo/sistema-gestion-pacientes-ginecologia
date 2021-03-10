package es.udc.fic.ginecologia.form;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.udc.fic.ginecologia.model.Schedule;
import es.udc.fic.ginecologia.model.SchedulePK;
import es.udc.fic.ginecologia.model.User;
import es.udc.fic.ginecologia.model.Weekday;

public class ScheduleConversor {
	public static ScheduleForm prepareScheduleForm(List<Schedule> schedules) {
		ScheduleForm scheduleForm = new ScheduleForm();
		List<ScheduleLine> scheduleLines = new ArrayList<>();

		for (int i = 0; i <= 4; i++) {
			ScheduleLine scheduleLine = new ScheduleLine();

			final Weekday expectedWeekday = chooseWeekday(i);

			Schedule foundSchedule = schedules.stream().filter(schedule -> schedule.getPk().getWeekday() == expectedWeekday)
					.findAny().orElse(null);

			if (foundSchedule != null) {
				scheduleLine.setSelected(true);
				scheduleLine.setWeekday(weekdayConversor(foundSchedule.getPk().getWeekday()));
				scheduleLine.setInitial_hour(foundSchedule.getInitial_hour());
				scheduleLine.setFinal_hour(foundSchedule.getFinal_hour());
			} else {
				scheduleLine.setSelected(false);
				scheduleLine.setWeekday(weekdayConversor(expectedWeekday));
				scheduleLine.setInitial_hour(LocalTime.of(0, 0, 1));
				scheduleLine.setFinal_hour(LocalTime.of(0, 0, 1));
			}

			scheduleLines.add(scheduleLine);
		}

		scheduleForm.setSchedules(scheduleLines);

		return scheduleForm;

	}

	public static Set<Schedule> convertToScheduleSet(List<ScheduleLine> schedulesInput, User user) {
		Set<Schedule> schedulesOutput = new HashSet<>();

		for (int i = 0; i <= 4; i++) {
			ScheduleLine scheduleLine = schedulesInput.get(i);

			if (scheduleLine.isSelected()) {
				Schedule schedule = new Schedule();
				
				SchedulePK PK = new SchedulePK();
				PK.setUser_id(user.getId());
				PK.setWeekday(chooseWeekday(i));
				
				schedule.setPk(PK);
				schedule.setInitial_hour(scheduleLine.getInitial_hour());
				schedule.setFinal_hour(scheduleLine.getFinal_hour());

				schedulesOutput.add(schedule);
			}
		}

		return schedulesOutput;
	}

	private static Weekday chooseWeekday(int i) {
		switch (i) {
			case 0:
				return Weekday.monday;
			case 1:
				return Weekday.tuesday;
			case 2:
				return Weekday.wednesday;
			case 3:
				return Weekday.thursday;
			case 4:
				return Weekday.friday;
			default:
				return null;
		}
	}
	
	private static int weekdayConversor(Weekday weekday) {
		switch (weekday) {
			case monday:
				return 0;
			case tuesday:
				return 1;
			case wednesday:
				return 2;
			case thursday:
				return 3;
			case friday:
				return 4;
			default:
				return -1;
		}
	}
}
