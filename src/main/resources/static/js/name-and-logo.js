$(document).ready(function() {

    $("#name-and-logo-form").validate({
        ignore: [],
        rules: {
			inputName: {
				required: true
			},
            logo: {
                required: false,
                extension: "png|jpe?g|gif",
				filesize: 1
            }
        }
    });

});