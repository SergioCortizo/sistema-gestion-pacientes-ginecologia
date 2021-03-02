// Call the dataTables jQuery plugin
$(document).ready(function() {
  $('#dataTable').DataTable({
        "language": {
            "lengthMenu": "Mostrar _MENU_ filas por página",
            "zeroRecords": "No hay filas.",
            "info": "Página _PAGE_ de _PAGES_",
            "infoEmpty": "No hay información disponible.",
            "infoFiltered": "(filtro de _MAX_ registros en total)",
			"paginate": {
		        "first": "Primero",
		        "last": "Último",
		        "next": "Siguiente",
		        "previous": "Anterior"
		    },
        },
		"searching" : false
    });
});
