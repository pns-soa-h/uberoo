package fr.unice.polytech.soa.uberoo.model.orders;

public class Restaurant {

	private Long restaurantId;

	private String name;


	public Restaurant() {

	}

	public Restaurant(Long id, String name) {
		this.restaurantId = id;
		this.name = name;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Restaurant{" +
				"id=" + restaurantId +
				", name='" + name + '\'' +
				'}';
	}
}
