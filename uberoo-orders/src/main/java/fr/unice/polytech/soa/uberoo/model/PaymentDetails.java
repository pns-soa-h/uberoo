package fr.unice.polytech.soa.uberoo.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PaymentDetails
 * 
 * Provides the informations about a payment Two main scenarios : - Pay by cash
 * - Pay by credit card
 *
 * @author Alexis Couvreur
 */
@Embeddable
public class PaymentDetails {

	private String methodId;
	private String methodTitle;
	private Boolean paid;

	public PaymentDetails() {
		// By default any payment is not paid
		paid = false;
	}

	public PaymentDetails(String methodId, String methodTitle) {
		this.methodId = methodId;
		this.methodTitle = methodTitle;
		this.paid = false;
	}

	/**
	 * @return the paid
	 */
	public Boolean getPaid() {
		return paid;
	}

	/**
	 * @param paid the paid to set
	 */
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	/**
	 * @return the methodTitle
	 */
	public String getMethodTitle() {
		return methodTitle;
	}

	/**
	 * @param methodTitle the methodTitle to set
	 */
	public void setMethodTitle(String methodTitle) {
		this.methodTitle = methodTitle;
	}

	/**
	 * @return the methodId
	 */
	public String getMethodId() {
		return methodId;
	}

	/**
	 * @param methodId the methodId to set
	 */
	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}

	@Override
	public String toString() {
		return "PaymentDetails{" +
				"methodId='" + methodId + '\'' +
				", methodTitle='" + methodTitle + '\'' +
				", paid=" + paid +
				'}';
	}
}
