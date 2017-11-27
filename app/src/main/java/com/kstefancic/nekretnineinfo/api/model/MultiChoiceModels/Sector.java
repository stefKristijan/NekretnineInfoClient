package com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels;


public class Sector {

	int id;
	String sectorName;

	@Override
	public String toString() {
		return "Sector [id=" + id + ", sectorName=" + sectorName + "]";
	}

	public Sector() {
	}

	public Sector(int id, String sectorName) {
		super();
		this.id = id;
		this.sectorName = sectorName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

}
