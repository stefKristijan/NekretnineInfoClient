package com.kstefancic.nekretnineinfo.api.model;

import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.City;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.State;
import com.kstefancic.nekretnineinfo.api.model.MultiChoiceModels.addressMultichoiceData.Street;

import java.util.List;


public class MultichoiceLocationDataResponse {

	List<City> cities;
	List<Street> streets;
	List<State> states;

	public MultichoiceLocationDataResponse(){}

	public MultichoiceLocationDataResponse(List<City> cities, List<Street> streets, List<State> states) {
		this.cities = cities;
		this.streets = streets;
		this.states = states;
	}



	@Override
	public String toString() {
		return "MultichoiceLocationDataResponse [cities=" + cities + ", streets=" + streets + ", states=" + states
				+ "]";
	}

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public List<Street> getStreets() {
		return streets;
	}

	public void setStreets(List<Street> streets) {
		this.streets = streets;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

}
