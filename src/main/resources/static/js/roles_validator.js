$.validator.addMethod("roles", function(value, elem, param) {
   return $(".roles:checkbox:checked").length > 0;
},"Seleccione por lo menos 1 rol.");