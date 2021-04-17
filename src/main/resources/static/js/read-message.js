function startInputs() {

	var config_input = {
		limit: 100
	};

	$('#subject').EnsureMaxLength(config_input);

}

$(document).ready(() => {

	startInputs();

	$('#users').multiselect({});

	$('#add-message-form').validate({
		rules: {
			subject: {
				required: true
			},
		}
	});

})