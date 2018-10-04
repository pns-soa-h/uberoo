package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Coursier;
import fr.unice.polytech.soa.uberoo.model.Order;

public class TimeETAMock implements TimeETA {

	@Override //Return a time in milisecond
	public long calculateOrderETA(Order O, Coursier c) {
		return 20*60*1000; //always return 20 minutes
	}

}
