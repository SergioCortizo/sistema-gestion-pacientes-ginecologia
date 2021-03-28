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

function startTextAreas() {

	var config_textarea = {
		limit: 1000
	};

	$('textarea[id^=input]').EnsureMaxLength(config_textarea);

}

$(document).ready(() => {

	startDateInputs();
	startTextAreas();

	$('#contraceptives').multiselect({});
	
	$('input[type="number"]').val(0);

	$("#register-form").validate({
		rules: {
			"DNI_NIF": {
				dniCheck: true
			},
			"mobile_phone": {
				phoneCheck: true
			},
			"landline": {
				phoneCheck: true
			},
		}
	});

})