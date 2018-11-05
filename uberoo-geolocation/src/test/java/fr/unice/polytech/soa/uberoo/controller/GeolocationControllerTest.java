package fr.unice.polytech.soa.uberoo.controller;

import com.jayway.jsonpath.JsonPath;
import fr.unice.polytech.soa.uberoo.repository.CoursierRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Alexis Couvreur on 10/5/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Ignore
public class GeolocationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CoursierRepository repository;

	public GeolocationControllerTest() {
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
	public void shouldSetStatusToInTransit() throws Exception {

		MvcResult result = createCoursierAndValidate();

		// Get order ref
		String updateLocation = JsonPath.parse(result.getResponse().getContentAsString()).read("$._links.update.href").toString();

		mockMvc.perform(patch(updateLocation)
				.content("{ \"location\": \"Vallauris\" }").contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());
	}

	private MvcResult createCoursierAndValidate() throws Exception {
		String request = "{\n" +
				"  \"id\" : 1,\n" +
				"  \"location\" : {\n" +
				"    \"place\" : \"Valbonne\"\n" +
				"  },\n" +
				"  \"_links\" : {\n" +
				"    \"self\" : {\n" +
				"      \"href\" : \"http://localhost:8080/coursiers/1\"\n" +
				"    },\n" +
				"    \"coursiers\" : {\n" +
				"      \"href\" : \"http://localhost:8080/coursiers\"\n" +
				"    },\n" +
				"    \"update\" : {\n" +
				"      \"href\" : \"http://localhost:8080/coursiers/1/location\"\n" +
				"    }\n" +
				"  }\n" +
				"}";
		return mockMvc.perform(get("/coursiers")
				.content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn();
	}

}
