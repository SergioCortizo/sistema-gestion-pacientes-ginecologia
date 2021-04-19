function startInputsCommonTasks() {

	var config_input = {
		limit: 100
	};

	$('#title').EnsureMaxLength(config_input);

}

$(document).ready(() => {

	startInputsCommonTasks()

	$('#users').multiselect({});
	
	$('#open-common-task-form').validate({});
	
});
