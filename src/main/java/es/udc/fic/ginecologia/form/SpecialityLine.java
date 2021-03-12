package es.udc.fic.ginecologia.form;

public class SpecialityLine {
	private Integer id;

	private String name;

	private boolean enabled;
	
	private UpdateSpecialityForm updateSpecialityForm;

	public SpecialityLine(Integer id, String name, boolean enabled) {
		this.id = id;
		this.name = name;
		this.enabled = enabled;
		this.updateSpecialityForm = new UpdateSpecialityForm(name);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public UpdateSpecialityForm getUpdateSpecialityForm() {
		return updateSpecialityForm;
	}

	public void setUpdateSpecialityForm(UpdateSpecialityForm updateSpecialityForm) {
		this.updateSpecialityForm = updateSpecialityForm;
	}

}
