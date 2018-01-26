package com.kstefancic.potresnirizik.api.model;

import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData.CadastralMunicipality;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData.City;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData.CityDistrict;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData.Settlement;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData.State;
import com.kstefancic.potresnirizik.api.model.MultiChoiceModels.addressMultichoiceData.Street;

import java.util.List;


public class MultichoiceLocationDataResponse {

	List<City> cities;
	List<Street> streets;
	List<State> states;
	List<Settlement> settlements;
	List<CityDistrict> cityDistricts;
	List<CadastralMunicipality> cadastralMunicipalities;

	public MultichoiceLocationDataResponse() {
	}

	public MultichoiceLocationDataResponse(List<City> cities, List<Street> streets, List<State> states,
										   List<Settlement> settlements, List<CityDistrict> cityDistricts,
										   List<CadastralMunicipality> cadastralMunicipalities) {
		super();
		this.cities = cities;
		this.streets = streets;
		this.states = states;
		this.settlements = settlements;
		this.cityDistricts = cityDistricts;
		this.cadastralMunicipalities = cadastralMunicipalities;
	}

	@Override
	public String toString() {
		return "MultichoiceLocationDataResponse [cities=" + cities + ", streets=" + streets + ", states=" + states
				+ ", settlements=" + settlements + ", cityDistricts=" + cityDistricts + ", cadastralMunicipalities="
				+ cadastralMunicipalities + "]";
	}

	public List<CadastralMunicipality> getCadastralMunicipalities() {
		return cadastralMunicipalities;
	}

	public void setCadastralMunicipalities(List<CadastralMunicipality> cadastralMunicipalities) {
		this.cadastralMunicipalities = cadastralMunicipalities;
	}

	public List<Settlement> getSettlements() {
		return settlements;
	}

	public void setSettlements(List<Settlement> settlements) {
		this.settlements = settlements;
	}

	public List<CityDistrict> getCityDistricts() {
		return cityDistricts;
	}

	public void setCityDistricts(List<CityDistrict> cityDistricts) {
		this.cityDistricts = cityDistricts;
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
