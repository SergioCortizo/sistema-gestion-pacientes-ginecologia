function timeToSeconds(time) {
	time = time.split(/:/);
	return time[0] * 3600 + time[1] * 60;
}

function startDateInputs() {
	var dates = $('input[id^=date]');

	var container = $('.bootstrap-iso form').length > 0 ? $('.bootstrap-iso form').parent() : "body";
	var options = {
		format: 'yyyy-mm-dd',
		startDate: new Date(),
		container: container,
		todayHighlight: true,
		autoclose: true,
		orientation: 'bottom',
		daysOfWeekDisabled: [0, 6]
	};

	dates.datepicker(options).on('changeDate', (e) => {
		var today = new Date();
		var actualHour = today.getHours() + ":" + today.getMinutes();
		var actualDate = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + today.getDate();

		var weekdays = ['sunday', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'];

		var weekday = weekdays[e.date.getDay()];

		var busyHours = [];

		var dateString = $("#dateEntry").val();

		var initialHour = $(".schedule[style!='display: none;'] ul.schedule-ul li." + weekday + " span.initialHour").first().text();
		var finalHour = $(".schedule[style!='display: none;'] ul.schedule-ul li." + weekday + " span.finalHour").first().text();

		$(".schedule[style!='display: none;'] ul.calEntries-ul li." + dateString + " span.calendar-meeting-hour").each((i, e) => {
			busyHours.push(e.innerText);
		});

		$("option[class^='hourOption']").each((i, e) => {

			$(".hourOption-" + i).hide();

			if ((timeToSeconds(initialHour) <= timeToSeconds(e.id) && timeToSeconds(finalHour) >= timeToSeconds(e.id))
				&& ((Date.parse(dateString) === Date.parse(actualDate) && timeToSeconds(e.id) > timeToSeconds(actualHour))
					|| Date.parse(dateString) != Date.parse(actualDate))
				&& (!busyHours.includes(e.id))) {
				$(".hourOption-" + i).show();
			}

		});

		if ($('#hourEntry').find(":selected").css('display') == 'none') {
			$("#hourEntry option[value='']").attr("selected", true);
		}

	});
}

$(document).ready(() => {

	startDateInputs();

	$('#hourEntry').multiselect({});

	$('#facultatives').multiselect({});

	$('#facultatives').change(() => {
		const scheduleId = $('#facultatives option').filter(':selected').val();

		var daysOfWeekDisabled = [0, 1, 2, 3, 4, 5, 6];
		var weekdays = ['sunday', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'];

		$('#schedule-' + scheduleId + " ul.schedule-ul li").each((i, e) => {
			var number = weekdays.indexOf(e.className);
			var index = daysOfWeekDisabled.indexOf(number);
			daysOfWeekDisabled.splice(index, 1);

			console.log(daysOfWeekDisabled);
		});

		$('#dateEntry').datepicker('setDaysOfWeekDisabled', daysOfWeekDisabled);

		$('div[id^="schedule-"]').hide();

		$('#schedule-' + scheduleId).show();

		if (scheduleId === "") {
			$('#hourEntryGroup').hide();
		} else {
			$('#hourEntryGroup').show();
		}

	});

	$('#update-calendar-entry-form').validate({
		rules: {
			facultative: {
				required: true
			},
			patient: {
				required: true
			},
			dateEntry: {
				required: true
			},
			hourEntry: {
				required: true
			}
		}
	});

	var today = new Date();
	var actualHour = today.getHours() + ":" + today.getMinutes();
	var actualDate = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + today.getDate();

	var weekdays = ['sunday', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'];

	var dateString = $("#dateEntry").val();
	var weekday = weekdays[new Date(dateString).getDay()];

	var busyHours = [];

	var initialHour = $(".schedule[style!='display: none;'] ul.schedule-ul li." + weekday + " span.initialHour").first().text();
	var finalHour = $(".schedule[style!='display: none;'] ul.schedule-ul li." + weekday + " span.finalHour").first().text();

	$(".schedule[style!='display: none;'] ul.calEntries-ul li." + dateString + " span.calendar-meeting-hour").each((i, e) => {
		busyHours.push(e.innerText);
	});

	console.log(busyHours);

	$("option[class^='hourOption']").each((i, e) => {

		$(".hourOption-" + i).hide();

		if ((timeToSeconds(initialHour) <= timeToSeconds(e.id) && timeToSeconds(finalHour) >= timeToSeconds(e.id))
			&& ((Date.parse(dateString) === Date.parse(actualDate) && timeToSeconds(e.id) > timeToSeconds(actualHour))
				|| Date.parse(dateString) != Date.parse(actualDate))
			&& (!busyHours.includes(e.id))) {
			$(".hourOption-" + i).show();
		}

	});

	if ($('#hourEntry').find(":selected").css('display') == 'none') {
		$("#hourEntry option[value='']").attr("selected", true);
	}
	
	const scheduleId = $('#facultatives option').filter(':selected').val();
	var daysOfWeekDisabled = [0, 1, 2, 3, 4, 5, 6];
	var weekdays = ['sunday', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday'];

	$('#schedule-' + scheduleId + " ul.schedule-ul li").each((i, e) => {
		var number = weekdays.indexOf(e.className);
		var index = daysOfWeekDisabled.indexOf(number);
		daysOfWeekDisabled.splice(index, 1);
	});
	
	$('#dateEntry').datepicker('setDaysOfWeekDisabled', daysOfWeekDisabled);

});