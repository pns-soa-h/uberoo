package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Order;

/**
 * Created by Picard Marchetto Ivan on 10/1/2018.
 */
public interface TimeETA {
	
	//Return a time in milisecond
	long calculateOrderETA(Order O, Long coursierId);
	
	
}
