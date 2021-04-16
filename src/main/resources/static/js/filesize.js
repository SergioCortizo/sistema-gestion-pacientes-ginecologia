$.validator.addMethod('filesize', function (value, element, param) {
    return this.optional(element) || (element.files[0].size / 1000 <= param)
}, 'El tamaño del archivo debe ocupar menos de {0} KB.');