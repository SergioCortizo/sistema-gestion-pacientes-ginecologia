$(document).ready(function() {
	var rules = {
		name: "required"
	};

	$("#add-medicine-form").validate({
		rules
	});
	
	$("[id^=update-medicine-form-]").each(function(i, e) {
		$("#" + e.id).validate({rules});
	});
	
});