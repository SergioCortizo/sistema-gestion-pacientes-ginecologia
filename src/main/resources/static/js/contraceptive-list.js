$(document).ready(function() {
	var rules = {
		name: "required"
	};

	$("#add-contraceptive-form").validate({
		rules
	});
	
	$("[id^=update-contraceptive-form-]").each(function(i, e) {
		$("#" + e.id).validate({rules});
	});
	
});