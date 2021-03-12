$(document).ready(function() {
	var rules = {
		name: "required"
	};

	$("#add-speciality-form").validate({
		rules
	});
	
	$("[id^=update-speciality-form-]").each(function(i, e) {
		$("#" + e.id).validate({rules});
	});
	
});