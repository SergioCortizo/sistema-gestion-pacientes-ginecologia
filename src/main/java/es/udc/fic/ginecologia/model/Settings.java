package es.udc.fic.ginecologia.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "settings")
public class Settings {

	@Id
	private String name;

	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		switch (name) {
			case "logo":
				return "Settings [name=" + name + "]";
			default:
				return "Settings [name=" + name + ", value=" + value + "]";
		}
	}

}
