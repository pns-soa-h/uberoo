package fr.unice.polytech.soa.uberoo.controller;

import com.jayway.jsonpath.JsonPath;
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

		mockMvc.perform(post("/orders")
				.content("{\"clientId\": \"0\", \"mealId\":\"2\", \"restaurantId\":\"5\"}").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.clientId").value("0"))
				.andExpect(jsonPath("$.mealId").value("2"))
				.andExpect(jsonPath("$.status").value("IN_PROGRESS"))
				.andExpect(jsonPath("$.coursierId").value(IsNull.nullValue()))
				.andExpect(jsonPath("$.eta").value(1200000));

	}


	@Test
	public void shouldRetrieveAllOrders() throws Exception {

		mockMvc.perform(post("/orders").content("{\"clientId\": \"0\", \"mealId\":\"2\", \"restaurantId\":\"5\"}").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		mockMvc.perform(post("/orders").content("{\"clientId\": \"1\", \"mealId\":\"3\", \"restaurantId\":\"1\"}").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		mockMvc.perform(post("/orders").content("{\"clientId\": \"2\", \"mealId\":\"1\", \"restaurantId\":\"7\"}").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

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

		String clientId = "0";
		String mealId = "2";
		String restaurantId = "12";
		MvcResult result = createOrderAndValidate(clientId, mealId, restaurantId);

		// Get order ref
		String selfOrder = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.self.href").toString();

		mockMvc.perform(get(selfOrder))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.clientId").value(clientId))
				.andExpect(jsonPath("$.mealId").value(mealId))
				.andExpect(jsonPath("$.restaurantId").value(restaurantId))
				.andExpect(jsonPath("$.status").value("IN_PROGRESS"))
				.andExpect(jsonPath("$.coursierId").value(IsNull.nullValue()))
				.andExpect(jsonPath("$.eta").value(1200000));


	}

	@Test
	public void shouldSetStatusToCompleted() throws Exception {

		String clientId = "0";
		String mealId = "2";
		String restaurantId = "12";
		MvcResult result = createOrderAndValidate(clientId, mealId, restaurantId);

		// Get order ref
		String completeOrder = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.complete.href").toString();

		 mockMvc.perform(put(completeOrder)
				.content("").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				 .andExpect(jsonPath("$.status").value("COMPLETED"));
	}

	@Test
	public void shouldThrowExceptionSettingStatusToCompleted() throws Exception {

		String clientId = "0";
		String mealId = "2";
		String restaurantId = "12";
		MvcResult result = createOrderAndValidate(clientId, mealId, restaurantId);

		// Get order ref
		String completeOrder = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.complete.href").toString();

		mockMvc.perform(put(completeOrder)
				.content("").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("COMPLETED"));

		mockMvc.perform(put(completeOrder)
				.content("").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void shouldSetStatusToCancelled() throws Exception {

		String clientId = "0";
		String mealId = "2";
		String restaurantId = "12";
		MvcResult result = createOrderAndValidate(clientId, mealId, restaurantId);

		// Get order ref
		String cancelOrder = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.cancel.href").toString();

		mockMvc.perform(delete(cancelOrder)
				.content("").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("CANCELLED"))
				.andReturn();

	}

	@Test
	public void shouldThrowExceptionSettingStatusToCancelled() throws Exception {

		String clientId = "0";
		String mealId = "2";
		String restaurantId = "12";
		MvcResult result = createOrderAndValidate(clientId, mealId, restaurantId);

		// Get order ref
		String cancelOrder = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.cancel.href").toString();

		mockMvc.perform(delete(cancelOrder)
				.content("").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("CANCELLED"))
				.andReturn();

		mockMvc.perform(delete(cancelOrder)
				.content("").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isMethodNotAllowed());
	}

	@Test
	public void shouldSetStatusToAssigned() throws Exception {

		String clientId = "0";
		String mealId = "2";
		String restaurantId = "12";
		MvcResult result = createOrderAndValidate(clientId, mealId, restaurantId);

		// Need to be completed to have assign link
		String completeOrder = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.complete.href").toString();

		MvcResult completeResult = mockMvc.perform(put(completeOrder)
				.content("").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("COMPLETED"))
				.andReturn();

		// Get order ref
		String assignOrder = JsonPath.parse(completeResult.getResponse().getContentAsString()).read("$._links.assign.href").toString();

		mockMvc.perform(put(assignOrder)
				.content("{\"coursierId\": \"12\"}").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("ASSIGNED"))
				.andExpect(jsonPath("$.coursierId").value("12"));

	}

	@Test
	public void shouldThrowExceptionSettingStatusToAssigned() throws Exception {

		String clientId = "0";
		String mealId = "2";
		String restaurantId = "12";
		MvcResult result = createOrderAndValidate(clientId, mealId, restaurantId);

		// Need to be completed to have assign link
		String completeOrder = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.complete.href").toString();

		MvcResult completeResult = mockMvc.perform(put(completeOrder)
				.content("").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("COMPLETED"))
				.andReturn();

		// Get order ref
		String assignOrder = JsonPath.parse(completeResult.getResponse().getContentAsString()).read("$._links.assign.href").toString();

		mockMvc.perform(put(assignOrder)
				.content("{\"coursierId\": \"12\"}").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("ASSIGNED"))
				.andExpect(jsonPath("$.coursierId").value("12"));

		mockMvc.perform(put(assignOrder)
				.content("{\"coursierId\": \"12\"}").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isMethodNotAllowed());
	}

	private MvcResult createOrderAndValidate(String clientId, String mealId, String restaurantId) throws Exception {
		return mockMvc.perform(post("/orders")
				.content("{\"clientId\": \"" + clientId + "\", \"mealId\":\"" + mealId + "\", \"restaurantId\": \"" + restaurantId + "\"}").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn();
	}

}
