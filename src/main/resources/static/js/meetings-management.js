function startDateInputs() {
	var dates = $('input[id^=date]');

	var container = $('.bootstrap-iso form').length > 0 ? $('.bootstrap-iso form').parent() : "body";
	var options = {
		format: 'dd/mm/yyyy',
		container: container,
		todayHighlight: true,
		autoclose: true,
	};

	dates.datepicker(options);
}

$(document).ready(() => {

	startDateInputs();
	
	$('#facultatives').multiselect({});
	$('#patients').multiselect({});
	
	$('#facultatives').change(() => {
		const scheduleId = $('#facultatives option').filter(':selected').val();
		
		$('div[id^="schedule-"]').hide();
		
		$('#schedule-' + scheduleId).show();
	});
	
	$('#add-calendar-entry-form').validate({
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
});