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
	
	$('#levels').multiselect({});
	
});