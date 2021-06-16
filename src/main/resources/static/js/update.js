$.validator.addMethod('lessThanEqual', function(value, element, param) {
	return this.optional(element) || value <= $(param).val() || $(param).val() === "";
}, "La hora inicial debe ser menor que la hora final");

$.validator.addMethod('greaterThanEqual', function(value, element, param) {
	return this.optional(element) || value >= $(param).val() || $(param).val() === "";
}, "La hora final debe ser mayor que la hora inicial");

$(document).ready(function() {
	$('.time-text').hunterTimePicker();

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

	$("#schedule-form").validate({});

	$("#schedule-form").submit((e) => {
		$("#schedule-alert").hide();
		
		if ($('#check-1').is(':checked') && !$(".checkbox-schedule").is(":checked")) {
			$("#schedule-alert").show();
			e.preventDefault();
		}
	});

	$("[id^=initialHour-]").each(function(i, e) {
		$("#" + e.id).rules("add", {
			lessThanEqual: "#finalHour-" + i
		});

		$("#finalHour-" + i).rules("add", {
			greaterThanEqual: "#" + e.id
		});
	});

	$("[id^=day-]").click((e) => {

		var id = e.target.id.match(/\d+$/);

		var initialHourId = "#initialHour-" + id;
		var finalHourId = "#finalHour-" + id;

		$(initialHourId).prop("disabled", !$(initialHourId).prop("disabled"));
		$(finalHourId).prop("disabled", !$(finalHourId).prop("disabled"));

	});

	$("#btnAddSpecialities").click(() => {
		$("#selectAddSpecialities").val().forEach(s => {
			$('#selectQuitSpecialities').append($("#speciality-" + s));
			$('#selectAddSpecialities').children('option[value=' + s + ']').remove();
		});
	});

	$("#btnQuitSpecialities").click(() => {
		$("#selectQuitSpecialities").val().forEach(s => {
			$('#selectAddSpecialities').append($("#speciality-" + s));
			$('#selectQuitSpecialities').children('option[value=' + s + ']').remove();
		});
	});

	$("#btnSubmitSpecialities").click(() => {
		$("#selectQuitSpecialities").children().attr("selected", "selected");
	});
});