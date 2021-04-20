function startTextAreas() {

	var config_textarea = {
		limit: 1000
	};

	$("#notice").EnsureMaxLength(config_textarea);

}

$(document).ready(() => {
	
	startTextAreas();
	
	$("#add-notice-form").validate({});
	
});