function startDateInputs() {
	var dates = $('input[id^=date]');

	var container = $('.bootstrap-iso form').length > 0 ? $('.bootstrap-iso form').parent() : "body";
	var options = {
		format: 'yyyy-mm-dd',
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
	$('#medicines').multiselect({});

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

		var new_row = $("<tr>"); dosification

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

	$("#add-medicine").click(() => {
		var selected_medicine = $("#medicines").children("option:selected").val();
		var text_medicine = $("#medicines").children("option:selected").text();

		if (selected_medicine === "0") {
			$('#error-recipe').show();
			return;
		}

		$('#error-recipe').hide();

		var length_table = $("#table-medicines tbody").find("tr").length;

		var length_recipe_list = $("#recipes-list li").length;

		var new_row = $("<tr>");

		var new_medicine = $("<th>");
		var val_medicine = $("<input type='hidden'>");
		new_medicine.attr('scope', 'row');
		val_medicine.attr('name', 'recipes[' + length_recipe_list + '].medicines[' + length_table + '].medicineId');
		val_medicine.val(selected_medicine);
		new_medicine.append("<p>" + text_medicine + "</p>");
		new_medicine.append(val_medicine);

		var denomination = $("<td>");
		var val_denomination = $("<input type='hidden'>");
		val_denomination.attr('name', 'recipes[' + length_recipe_list + '].medicines[' + length_table + '].denomination');
		val_denomination.val($("#denomination").val());
		denomination.append("<p>" + $("#denomination").val() + "</p>");
		denomination.append(val_denomination);

		var dosification = $("<td>");
		var val_dosification = $("<input type='hidden'>");
		val_dosification.attr('name', 'recipes[' + length_recipe_list + '].medicines[' + length_table + '].dosification');
		val_dosification.val($("#dosification").val());
		dosification.append("<p>" + $("#dosification").val() + "</p>");
		dosification.append(val_dosification);

		var formofadministration = $("<td>");
		var val_formofadministration = $("<input type='hidden'>");
		val_formofadministration.attr('name', 'recipes[' + length_recipe_list + '].medicines[' + length_table + '].formOfAdministration');
		val_formofadministration.val($("#formOfAdministration").val());
		formofadministration.append("<p>" + $("#formOfAdministration").val() + "</p>");
		formofadministration.append(val_formofadministration);

		var posology = $("<td>");
		var val_posology = $("<input type='hidden'>");
		val_posology.attr('name', 'recipes[' + length_recipe_list + '].medicines[' + length_table + '].posology');
		val_posology.val($("#posology").val());
		posology.append("<p>" + $("#posology").val() + "</p>");
		posology.append(val_posology);

		var format = $("<td>");
		var val_format = $("<input type='hidden'>");
		val_format.attr('name', 'recipes[' + length_recipe_list + '].medicines[' + length_table + '].format');
		val_format.val($("#format").val());
		format.append("<p>" + $("#format").val() + "</p>");
		format.append(val_format);

		var units = $("<td>");
		var val_units = $("<input type='hidden'>");
		val_units.attr('name', 'recipes[' + length_recipe_list + '].medicines[' + length_table + '].units');
		val_units.val($("#units").val());
		units.append("<p>" + $("#units").val() + "</p>");
		units.append(val_units);

		new_row.append(new_medicine);
		new_row.append(denomination);
		new_row.append(dosification);
		new_row.append(formofadministration);
		new_row.append(format);
		new_row.append(units);
		new_row.append(posology);

		$("#table-medicines tbody").append(new_row);

		$("#medicines").val("0");
		$("#denomination").val("");
		$("#dosification").val("");
		$("#formOfAdministration").val("");
		$("#posology").val("");
		$("#format").val("0");
		$("#units").val("0");

	});

	$("#add-recipe").click(() => {
		tinyMCE.triggerSave();

		var length_recipe_list = $("#recipes-list li").length;

		$("#no-recipes-added").hide();

		var new_recipe = $("<li>");

		var date_dispensing_string = $("#dateDispensing").val();

		if (!date_dispensing_string) {
			var today = new Date();
			var dd = String(today.getDate()).padStart(2, '0');
			var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
			var yyyy = today.getFullYear();
			
			date_dispensing_string = yyyy + '-' + mm + '-' + dd;
		}

		var dispensing_date_label = $("#recipe-dispensed-for-label-hidden").clone();
		dispensing_date_label.show();
		new_recipe.append(dispensing_date_label);
		var date_dispensing_text = $("<span>");
		date_dispensing_text.text(date_dispensing_string);
		date_dispensing_text.addClass("ml-2");
		var date_dispensing_value = $("<input type='hidden'>");
		date_dispensing_value.val(date_dispensing_string);
		date_dispensing_value.attr('name', 'recipes[' + length_recipe_list + '].dispensingDate');
		new_recipe.append(date_dispensing_text);
		new_recipe.append(date_dispensing_value);

		var recipe_content = $("<ul>");

		var recipe_clarifications = $("<li>");
		var clarifications_label = $("#clarifications-label-hidden").clone();
		clarifications_label.show();
		recipe_clarifications.append(clarifications_label);
		var clarifications_text = $("<div>");
		clarifications_text.append($("#clarifications").val());
		recipe_clarifications.append(clarifications_text);
		var clarifications_val = $("<input type='text'>");
		clarifications_val.hide();
		clarifications_val.attr('name', 'recipes[' + length_recipe_list + '].clarifications');
		clarifications_val.val($("#clarifications").val());
		recipe_clarifications.append(clarifications_val);

		recipe_content.append(recipe_clarifications);

		var recipe_medicines = $("<li>");

		var medicines_label = $("#medicines-label-hidden").clone();
		medicines_label.show();
		recipe_medicines.append(medicines_label);
		var table_medicines = $("#table-medicines").clone();
		table_medicines.removeClass();
		table_medicines.addClass("table table-sm table-responsive");
		recipe_medicines.append(table_medicines);

		recipe_content.append(recipe_medicines);

		new_recipe.append(recipe_content);
		$("#recipes-list").append(new_recipe);

		$("#table-medicines-content").empty();
		$("#dateDispensing").val("");
		tinyMCE.get('clarifications').setContent('');
	});

})