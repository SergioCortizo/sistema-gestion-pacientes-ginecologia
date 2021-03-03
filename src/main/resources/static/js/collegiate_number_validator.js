$.validator.addMethod("collegiateNumberCheck", function(value, element) {
	return this.optional(element) || /[0-5][0-2][0-5][0-2][0-9]{5}/.test(value);
}, "Numero de colegiado no valido.");