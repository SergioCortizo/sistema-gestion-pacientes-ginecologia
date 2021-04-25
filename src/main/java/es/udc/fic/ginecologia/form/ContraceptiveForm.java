package es.udc.fic.ginecologia.form;

public class ContraceptiveForm {
	private String name;

	private boolean enabled;

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

	@Override
	public String toString() {
		return "ContraceptiveForm [name=" + name + ", enabled=" + enabled + "]";
	}
	
}
