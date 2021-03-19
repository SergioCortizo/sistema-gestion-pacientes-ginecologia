$(document).ready(function() {
	var rules = {
		name: "required"
	};

	$("#add-diagnostic-test-form").validate({
		rules
	});
	
	$("[id^=update-diagnostic-test-form-]").each(function(i, e) {
		$("#" + e.id).validate({rules});
	});
	
});