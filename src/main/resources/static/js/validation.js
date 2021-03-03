$.extend(
	$.validator.messages, {
		minlength: jQuery.validator.format("Por favor meta al menos {0} caracteres."),
		maxlength: jQuery.validator.format("El limite de caracteres esta en {0} caracteres."),
		required: jQuery.validator.format("Campo requerido."),
		email: jQuery.validator.format("Por favor introduzca un email válido."),
		equalTo: jQuery.validator.format("Por favor introduzca el mismo valor de nuevo."),
	}
);

$.validator.setDefaults({
    highlight: (element) => $(element).closest('.form-control').addClass('is-invalid'),
    unhighlight: (element) => $(element).closest('.form-control').removeClass('is-invalid'),
    errorElement: 'span',
    errorClass: 'label label-danger',
    errorPlacement: (error, element) => 
		element.parent('.input-group').length ? 
			error.insertAfter(element.parent()) : 
			error.insertAfter(element)
});