function startDateInputs() {
	var dates = $('input[id^=date]');

	var container = $('.bootstrap-iso form').length > 0 ? $('.bootstrap-iso form').parent() : "body";
	var options = {
		format: 'dd/mm/yyyy',
		container: container,
		todayHighlight: true,
		autoclose: true,
	};

	dates.datepicker(options);
}

function startTextAreas() {

	var config_textarea = {
		limit: 1000
	};

	$('textarea[id^=input]').EnsureMaxLength(config_textarea);
	$("#answer-input").EnsureMaxLength(config_textarea);

}

$(document).ready(() => {

	startDateInputs();
	startTextAreas();

	$('#contraceptives').multiselect({});

	$("#update-form").validate({
		rules: {
			"DNI_NIF": {
				dniCheck: true
			},
			"mobile_phone": {
				phoneCheck: true
			},
			"landline": {
				phoneCheck: true
			},
		}
	});

	$("#add-meeting-form").validate({});

	$("#add-meeting-submit").click(() => {
		$("#result").html($("#comments").val());
	});

	$("#dropdown-questions li").on('click', function() {
		var txt = ($(this).text());
		$("#question-input").val(txt.trim());
	});

	$(".custom-file-input").on("change", function() {
		var fileName = $(this).val().split("\\").pop();
		$(this).siblings(".custom-file-label").addClass("selected").html(fileName);
	});

	$("#add-question").click(() => {
		if ($('#question-input').val() === "" | $('#answer-input').val() === "") {
			$('#error-question').show();
			return;
		}

		$('#error-question').hide();

		var length_table = $("#table-questions tbody").find("tr").length;

		var new_row = $("<tr>");

		var new_question = $("<th>");
		var val_question = $("<input type='hidden'>");
		new_question.attr('scope', 'row');
		val_question.attr('name', "questions[" + length_table + "]");
		val_question.val($("#question-input").val());
		new_question.append("<p>" + $("#question-input").val() + "</p>");
		new_question.append(val_question);

		var new_answer = $("<td>");
		var val_answer = $("<input type='hidden'>");
		val_answer.attr('name', "answers[" + length_table + "]");
		val_answer.val($("#answer-input").val());
		new_answer.append("<p>" + $("#answer-input").val() + "</p>");
		new_answer.append(val_answer);

		new_row.append(new_question);
		new_row.append(new_answer);

		$("#table-questions tbody").append(new_row);

		$("#question-input").val("");
		$("#answer-input").val("");

	});

	$("#add-diagnostic-test").click(() => {
		var selected_diagnostic_test = $("#diagnostic-test").children("option:selected").val();
		var text_diagnostic_test = $("#diagnostic-test").children("option:selected").text();
		var fileName = $('#file-diagnostic-test').val().split("\\").pop();

		if (selected_diagnostic_test === "0" | $('#file-diagnostic-test').get(0).files.length === 0) {
			$('#error-diagnostic-test').show();
			return;
		}

		$('#error-diagnostic-test').hide();
		
		var length_table = $("#table-diagnostic-tests tbody").find("tr").length;

		var new_row = $("<tr>");

		var new_diagnostic_test = $("<th>");
		var val_diagnostic_test = $("<input type='hidden'>");
		new_diagnostic_test.attr('scope', 'row');
		val_diagnostic_test.attr('name', "diagnosticTestIds[" + length_table + "]");
		val_diagnostic_test.val(selected_diagnostic_test);
		new_diagnostic_test.append("<p>" + text_diagnostic_test + "</p>");
		new_diagnostic_test.append(val_diagnostic_test);
		
		var new_file = $("<td>");
		var val_file = $('#file-diagnostic-test').clone();
		val_file.attr('name', "files[" + length_table + "]");
		val_file.hide();
		new_file.append("<p>" + fileName + "</p>");
		new_file.append(val_file);
		
		new_row.append(new_diagnostic_test);
		new_row.append(new_file);
		
		$("#table-diagnostic-tests tbody").append(new_row);

		$("#diagnostic-test").val("0");
		$("#file-diagnostic-test").val("");
		$(".custom-file-input").siblings(".custom-file-label").removeClass("selected").html("");
	});

})