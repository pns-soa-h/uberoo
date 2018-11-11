package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.*;

@Embeddable
public class Location {

	@Column(name = "place")
	private String place;

	public Location(){

	}

	public Location(String place) {
		this.place = place;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
}
