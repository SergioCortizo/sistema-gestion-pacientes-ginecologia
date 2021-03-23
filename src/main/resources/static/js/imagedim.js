$.validator.addMethod('imagedim', function(value, element, param) {
	
	var width = $(element).data('imageWidth');
	var height = $(element).data('imageHeight');
	
	return width <= param[0] && height <= param[1];
}, 'La resolución máxima es de {0} x {1} píxeles');