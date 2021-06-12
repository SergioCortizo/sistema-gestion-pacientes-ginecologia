$(document).ready(function() {
	$("#update-form").validate({
		rules: {
			"inputDNI": {
				dniCheck: true
			},
			"inputPhoneNumber": {
				phoneCheck: true
			},
			"inputCollegiateNumber": {
				collegiateNumberCheck: true
			},
			"roles": {
				required: true, minlength: 1
			}
		},
		messages: {
			"roles": "Por favor escoja más de un rol."
		},
		errorPlacement: function(error, element) {
			console.log('dd', element.attr("name"))
			if (element.attr("name") == "roles") {
				error.appendTo("#checkboxes");
			} else {
				error.insertAfter(element)
			}
		},
	}).settings.rules;

	$("#password-form").validate({
		rules: {
			inputNewPassword: "required",
			"inputConfirmPassword": {
				equalTo: "#inputNewPassword"
			},
		},
	});
});