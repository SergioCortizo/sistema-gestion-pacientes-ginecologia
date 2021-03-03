$.validator.addMethod("dniCheck", function(value, element) {
	
  if(/^([0-9]{8})*[a-zA-Z]+$/.test(value)){
    var numero = value.substr(0,value.length-1);
    var let = value.substr(value.length-1,1).toUpperCase();

    numero = numero % 23;

    var letra='TRWAGMYFPDXBNJZSQVHLCKET';

    letra = letra.substring(numero,numero+1);
    if (letra==let) return true;
    return false;
  }

  return this.optional(element);
}, "DNI no válido");