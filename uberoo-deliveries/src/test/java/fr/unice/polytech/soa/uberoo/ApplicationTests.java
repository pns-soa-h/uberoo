package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.*;
import fr.unice.polytech.soa.uberoo.repository.CoursierRepository;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CoursierRepository coursierRepository;

	private Coursier coursier;
	private Order order;

	@Before
	public void initAllTests() {
		this.orderRepository.deleteAll();
		this.coursierRepository.deleteAll();
		this.coursier = new Coursier("Roger");
		this.coursierRepository.save(this.coursier);
		this.order = new Order();
		this.order.setClientId((long)38);
		Meal meal = new Meal();
		meal.setLabel("a");
		Address address = new Address("2255 Route des Dolines", null, "Valbonne", "06560", "France");
		Restaurant restaurant = new Restaurant((long)8, "Joli resto", address);
		meal.setRestaurant(restaurant);
		this.order.setId((long)1);
		this.order.setMeals(Collections.singletonList(meal));
		this.order.setETA((long)5);
		this.orderRepository.save(this.order);
	}

	@Test
	public void coursierAvailableTest() throws Exception {
		this.mockMvc.perform(get("/coursiers"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.coursiers").exists());
	}

	@Test
	public void orderDisplaysCoursier() throws Exception {
		this.mockMvc.perform(get("/orders/"+this.order.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.coursier").hasJsonPath());
	}

	@Test
	public void assignOrderToCoursier() throws Exception {
		this.mockMvc.perform(patch("/orders/"+this.order.getId())
					.content("{" +
								"\"id\": " + this.coursier.getId() + "," +
								"\"name\": \"" + this.coursier.getName() + "\"}")
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.coursier").exists())
				.andExpect(jsonPath("$.coursier").value(this.coursier));
	}
}
