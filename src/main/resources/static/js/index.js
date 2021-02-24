function startDateInputs() {
	var date_from=$('input[name="dateFrom"]');
	var date_to=$('input[name="dateTo"]');
					  
	var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
	var options={
		format: 'dd/mm/yyyy',
		container: container,
		todayHighlight: true,
		autoclose: true,
	};
			
	date_from.datepicker(options);
	date_to.datepicker(options);
}
	
$(document).ready(startDateInputs())