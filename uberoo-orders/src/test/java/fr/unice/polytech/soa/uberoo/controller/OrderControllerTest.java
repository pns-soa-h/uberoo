package fr.unice.polytech.soa.uberoo.controller;

import com.jayway.jsonpath.JsonPath;
import fr.unice.polytech.soa.uberoo.model.*;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private OrderRepository repository;

	private ShippingAddress shippingAddress;
	private BillingAddress billingAddress;
	private String clientId;
	private Restaurant restaurant;
	private Meal meal;

	public OrderControllerTest() {
		clientId = "15"; // arbitrary
		shippingAddress = new ShippingAddress("Alexis", "Couvreur", null, "2255 Route des Dolines", null, "Valbonne", "06560", "France", "alexis.couvreur@etu.unice.fr", "0612345678");
		billingAddress = shippingAddress; // My billing address is the same
		restaurant = new Restaurant(12L, "Le Bon Burger", new Address("1 Place Joseph Bermond", null, "Valbonne", "06560", "France"));
		meal = new Meal(42L, 2);
	}

	@ClassRule
	public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, "order");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("spring.kafka.bootstrap-servers", embeddedKafka.getBrokersAsString());
	}

	@Before
	public void deleteAllBeforeTests() {
		repository.deleteAll();
	}

	@Test
	public void shouldReturnRepositoryIndex() throws Exception {
		mockMvc.perform(get("/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.orders").exists());
	}

	@Test
	public void shouldCreateOrder() throws Exception {

		/*mockMvc.perform(post("/orders")
				.content("{\"clientId\": \"0\", \"mealId\":\"2\", \"restaurantId\":\"5\"}").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.clientId").value("0"))
				.andExpect(jsonPath("$.mealId").value("2"))
				.andExpect(jsonPath("$.status").value("IN_PROGRESS"))
				.andExpect(jsonPath("$.coursierId").value(IsNull.nullValue()))
				.andExpect(jsonPath("$.eta").value(1200000));*/
		createOrderAndValidate(clientId, meal, restaurant, shippingAddress, billingAddress);
	}


	@Test
	public void shouldRetrieveAllOrders() throws Exception {

		Restaurant r5 = new Restaurant(5L, "Le poisson gourmand", new Address("1 Place Joseph Bermond", null, "Valbonne", "06560", "France"));
		Restaurant r7 = new Restaurant(7L, "L'assiette creuse", new Address("1 Place Joseph Bermond", null, "Valbonne", "06560", "France"));

<<<<<<< HEAD
		Meal m2 = new Meal(2L, 1);
		Meal m5 = new Meal(5L, 4);
=======
		Meal m2 = new Meal(2L, "Baguette", "Une baguette quoi", 0.99);
		Meal m5 = new Meal(5L, "Croissant", "Un croissant au beurre...", 0.59);
>>>>>>> 0ea984b... Post orders from gateway

		createOrderAndValidate(clientId, meal, restaurant, shippingAddress, billingAddress);
		createOrderAndValidate(clientId, m2, r5, shippingAddress, billingAddress);
		createOrderAndValidate(clientId, m5, r7, shippingAddress, billingAddress);

		mockMvc.perform(get("/orders"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.orders").value(hasSize(3)));

		mockMvc.perform(get("/orders?restaurant=5"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.orders").value(hasSize(1)));

		mockMvc.perform(get("/orders?status=IN_PROGRESS"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.orders").value(hasSize(3)));

	}

	@Test
	public void shouldRetrieveOneOrder() throws Exception {

		MvcResult result = createOrderAndValidate(clientId, meal, restaurant, shippingAddress, billingAddress);

		// Get order ref
		String selfOrder = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.self.href").toString();

		mockMvc.perform(get(selfOrder))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.clientId").value(clientId))
				.andExpect(jsonPath("$.meal.mealId").value(meal.getMealId()))
				.andExpect(jsonPath("$.restaurant.restaurantId").value(restaurant.getId()))
				.andExpect(jsonPath("$.status").value("IN_PROGRESS"))
				.andExpect(jsonPath("$.coursierId").value(IsNull.nullValue()))
				.andExpect(jsonPath("$.eta").value(1200000));


	}

	@Test
	public void shouldSetStatusToAccepted() throws Exception {

		MvcResult result = createOrderAndValidate(clientId, meal, restaurant, shippingAddress, billingAddress);

		// Get order ref
		String updateStatus = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.update.href").toString();

		 mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"ACCEPTED\", \"payment_method\": \"cb\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				 .andExpect(jsonPath("$.status").value("ACCEPTED"));
	}

	@Test
	public void shouldThrowExceptionSettingStatusToCompleted() throws Exception {

		MvcResult result = createOrderAndValidate(clientId, meal, restaurant, shippingAddress, billingAddress);

		// Get order ref
		String updateStatus = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.update.href").toString();

		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"ACCEPTED\", \"payment_method\": \"cb\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("ACCEPTED"));

		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"ACCEPTED\", \"payment_method\": \"cb\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void shouldSetStatusToCancelled() throws Exception {

		MvcResult result = createOrderAndValidate(clientId, meal, restaurant, shippingAddress, billingAddress);

		// Get order ref
		String updateStatus = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.update.href").toString();

		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"CANCELLED\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("CANCELLED"));
	}

	@Test
	public void shouldThrowExceptionSettingStatusToCancelled() throws Exception {

		MvcResult result = createOrderAndValidate(clientId, meal, restaurant, shippingAddress, billingAddress);

		// Get order ref
		String updateStatus = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.update.href").toString();

		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"CANCELLED\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("CANCELLED"));

		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"CANCELLED\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void shouldSetStatusToInTransit() throws Exception {

		MvcResult result = createOrderAndValidate(clientId, meal, restaurant, shippingAddress, billingAddress);

		// Get order ref
		String updateStatus = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.update.href").toString();

		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"ACCEPTED\", \"payment_method\": \"cb\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		//Restaurant preparing
		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"IN_PREPARATION\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"IN_TRANSIT\", \"coursierId\": \"12\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("IN_TRANSIT"));

	}

	@Test
	public void shouldThrowExceptionSettingStatusToInTransit() throws Exception {

		MvcResult result = createOrderAndValidate(clientId, meal, restaurant, shippingAddress, billingAddress);

		// Get order ref
		String updateStatus = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.update.href").toString();

		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"ACCEPTED\", \"payment_method\": \"cb\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		//Restaurant preparing
		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"IN_PREPARATION\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"IN_TRANSIT\", \"coursierId\": \"12\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("IN_TRANSIT"));

		mockMvc.perform(patch(updateStatus)
				.content("{ \"status\": \"IN_TRANSIT\", \"coursierId\": \"12\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isMethodNotAllowed());
	}

	private MvcResult createOrderAndValidate(String clientId, Meal meal, Restaurant restaurant, ShippingAddress shipping, BillingAddress billing) throws Exception {

		String request =
				"{\"clientId\": \"" + clientId + "\"" +
				", \"meal\": {" +
<<<<<<< HEAD
					"\"id\": \"" + meal.getId() + "\"" +
					", \"quantity\": \"" + meal.getQuantity() + "\"" +
=======
					"\"mealId\": \"" + meal.getMealId() + "\"" +
					", \"name\": \"" + meal.getName() + "\"" +
					", \"description\": \"" + meal.getDescription() + "\"" +
					", \"price\": \"" + meal.getPrice() + "\"" +
>>>>>>> 0ea984b... Post orders from gateway
				"}" +
				", \"restaurant\": {" +
					"\"restaurantId\": \"" + restaurant.getId() + "\"" +
					", \"name\": \"" + restaurant.getName() + "\"" +
					", \"address\": {" +
						"\"address_1\": \"" + restaurant.getAddress().getAddress_1() + "\"" +
						// ", \"address_2\": \"" + restaurant.getAddress().getAddress_2() + "\"" +
						", \"city\": \"" + restaurant.getAddress().getCity() + "\"" +
						", \"postcode\": \"" + restaurant.getAddress().getPostcode() + "\"" +
						", \"country\": \"" + restaurant.getAddress().getCountry() + "\"" +
					"}" +
				"}" +
				", \"shippingAddress\": {" +
					"\"firstName\": \"" + shipping.getFirstName() + "\"" +
					", \"lastName\": \"" + shipping.getLastName() + "\"" +
					", \"address_1\": \"" + shipping.getAddress_1() + "\"" +
					", \"address_2\": \"" + shipping.getAddress_2() + "\"" +
					", \"city\": \"" + shipping.getCity() + "\"" +
					", \"postcode\": \"" + shipping.getPostcode() + "\"" +
					", \"country\": \"" + shipping.getCountry() + "\"" +
					", \"email\": \"" + shipping.getEmail() + "\"" +
					", \"phone\": \"" + shipping.getPhone() + "\"" +
				"}" +
				", \"billingAddress\": {" +
					"\"firstName\": \"" + billing.getFirstName() + "\"" +
					", \"lastName\": \"" + billing.getLastName() + "\"" +
					", \"address_1\": \"" + billing.getAddress_1() + "\"" +
					", \"address_2\": \"" + billing.getAddress_2() + "\"" +
					", \"city\": \"" + billing.getCity() + "\"" +
					", \"postcode\": \"" + billing.getPostcode() + "\"" +
					", \"country\": \"" + billing.getCountry() + "\"" +
				"}}" ;
		return mockMvc.perform(post("/orders")
				.content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn();
	}

}
