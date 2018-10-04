package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Order;
import fr.unice.polytech.soa.uberoo.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
    private OrderRepository repository;

    private Order order;

    @Before
    public void deleteAllBeforeTests() {
        repository.deleteAll();
        order = repository.save(new Order(0L, 1L));
    }

    @Test
    public void shouldReturnRepositoryIndex() throws Exception {

        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
                jsonPath("$._links.orders").exists());
    }

    @Test
    public void shouldCreateOrder() throws Exception {
        mockMvc.perform(post("/orders/new").content(
                "{\"clientId\": \"0\", \"mealId\":\"2\"}").contentType(MediaType.APPLICATION_JSON)).andExpect(
                status().isCreated());
    }

    @Test
    public void shouldRetrieveOrderList() throws Exception {

        mockMvc.perform(post("/orders/new").content(
                "{\"clientId\": \"0\", \"mealId\":\"2\"}").contentType(MediaType.APPLICATION_JSON)).andExpect(
                status().isCreated());
        mockMvc.perform(get("/orders")).andDo(print()).andExpect(status().isOk());

    }

    @Test
    public void shouldRetrieveOrder() throws Exception {

        mockMvc.perform(get("/orders/" + order.getId())).andExpect(status().isOk()).andExpect(
                jsonPath("$.clientId").value("0")).andExpect(
                jsonPath("$.mealId").value("1")
        );
    }

    @Test
    public void shouldAssignCoursier() throws Exception {

        mockMvc.perform(put("/orders/" + order.getId()).content(
                "{\"coursierId\": \"12\"}").contentType(MediaType.APPLICATION_JSON)).andExpect(
                status().isOk()).andExpect(
                jsonPath("$.coursierId").value("12")
        );

        mockMvc.perform(get("/orders/" + order.getId())).andExpect(status().isOk()).andExpect(
                jsonPath("$.clientId").value("0")).andExpect(
                jsonPath("$.mealId").value("1")).andExpect(
                jsonPath("$.coursierId").value("12")
        );
    }

}
