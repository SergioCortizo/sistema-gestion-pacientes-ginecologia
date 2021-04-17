tinymce.init({
	language: "es",
	selector: "#comments",
	plugins: 'lists advlist',
	toolbar: toolbar + ' numlist bullist'
});

tinymce.init({
	language: "es",
	selector: "#clarifications",
	plugins: 'lists advlist',
	toolbar: toolbar + ' numlist bullist'
});

tinymce.init({
	language: "es",
	selector: "#messagebody",
	plugins: 'lists advlist',
	width: '100%',
});

tinymce.init({
	language: "es",
	selector: "#interconsultationbody",
	plugins: 'lists advlist',
	width: '100%',
});