$(document).ready(function() {

	$("#restore-database-form").validate({
		ignore: [],
		rules: {
			backupFile: {
				required: true,
				extension: "sql",
			}
		}
	});

});