package es.udc.fic.ginecologia.form;

public class DiagnosticTestForm {
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
		return "DiagnosticTestForm [name=" + name + ", enabled=" + enabled + "]";
	}
	
}
