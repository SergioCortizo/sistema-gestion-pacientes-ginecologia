$.validator.addMethod("phoneCheck", function(value, element) {
	return this.optional(element) || /[0-9]{9}/.test(value);
}, "Teléfono no válido");