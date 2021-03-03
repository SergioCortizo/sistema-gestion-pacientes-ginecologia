$(document).ready(function() {
	$("#register-form").validate({
		rules: {
			inputPassword: "required",
			"inputConfirmPassword": {
				equalTo: "#inputPassword"
			},
			"inputDNI": {
				dniCheck: true
			},
			"inputPhoneNumber": {
				phoneCheck: true
			},
			"inputCollegiateNumber": {
				collegiateNumberCheck: true
			},
			"roles[]": {
				required: true
			}
		},
		messages: {
			"roles[]" : "Por favor escoja más de un rol."
		},
		errorPlacement: function (error, element) {
            console.log('dd', element.attr("name"))
            if (element.attr("name") == "roles[]") {
                error.appendTo("#checkboxes");
            } else {
                error.insertAfter(element)
            }
        },
	});
});