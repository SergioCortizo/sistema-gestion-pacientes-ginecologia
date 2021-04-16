$.validator.addMethod('imagedim', (value, element, param) => {

	if ($(element).get(0).files.length === 0) {
		console.log("No files selected.");
		return true;
	}

	var width = $(element).data('imageWidth');
	var height = $(element).data('imageHeight');

	return width <= param[0] && height <= param[1];
}, 'La resolución máxima es de {0} x {1} píxeles');