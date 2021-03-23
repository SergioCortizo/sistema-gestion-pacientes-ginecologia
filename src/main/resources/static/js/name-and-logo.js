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
				filesize: 1,
				imagedim: [200, 200]
			}
		}
	});
	
	$('#logo').change(function() {
		$('#logo').removeData('imageWidth');
		$('#logo').removeData('imageHeight');
		
		var file = this.files[0];
		var tmpImg = new Image();
		tmpImg.src = window.URL.createObjectURL(file);
		
		tmpImg.onload = function() {
			width = tmpImg.naturalWidth,
				height = tmpImg.naturalHeight;
			$('#logo').data('imageWidth', width);
			$('#logo').data('imageHeight', height);
		}
	});


});