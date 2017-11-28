package com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels;

public class Purpose {

	private int id;
	private String purpose;
	private Sector sector;

	@Override
	public String toString() {
		return "Purpose{" +
				"id=" + id +
				", purpose='" + purpose + '\'' +
				", sector=" + sector +
				'}';
	}

	public Purpose() {
	}

	public Purpose(int id, String purpose) {
		this.id = id;
		this.purpose = purpose;
	}


	public int getId() {
		return id;
	}

	public Sector getSector() {
		return sector;
	}

	public void setSector(Sector sector) {
		this.sector = sector;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
}
