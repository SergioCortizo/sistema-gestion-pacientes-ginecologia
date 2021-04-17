function startInputsInterconsultation() {

	var config_input = {
		limit: 100
	};

	$('#reasonInterconsultation').EnsureMaxLength(config_input);

}

$(document).ready(() => {

	startInputsInterconsultation();

	$('#usersInterconsultation').multiselect({});

	$('#usersInterconsultation').change(() => {
		const specialitiesId = $('#usersInterconsultation option').filter(':selected').val();

		$('ul[id^="specialities-"]').hide();

		$('#specialities-' + specialitiesId).show();
	});

	$('#open-interconsultation-form').validate({
		rules: {
			reasonInterconsultation: {
				required: true
			},
		}
	});

})