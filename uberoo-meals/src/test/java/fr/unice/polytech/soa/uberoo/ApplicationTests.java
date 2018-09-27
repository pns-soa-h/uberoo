package fr.unice.polytech.soa.uberoo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MealRepository mealRepository;

    @Before
    public void deleteAllBeforeTests() throws Exception {
        mealRepository.deleteAll();
    }

    @Test
    public void shouldReturnRepositoryIndex() throws Exception {

        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
                jsonPath("$._links.meal").exists());
    }

    @Test
    public void shouldCreateEntity() throws Exception {

        mockMvc.perform(post("/meal").content(
                "{\"label\": \"Ramen\", \"description\":\"Japanese dish\"}")).andExpect(
                status().isCreated()).andExpect(
                header().string("Location", containsString("meal/")));
    }

    @Test
    public void shouldRetrieveEntity() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/meal").content(
                "{\"label\": \"Ramen\", \"description\":\"Japanese dish\"}")).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.label").value("Ramen")).andExpect(
                jsonPath("$.description").value("Japanese dish"));
    }

    @Test
    public void shouldQueryEntity() throws Exception {

        mockMvc.perform(post("/meal").content(
                "{\"label\": \"Ramen\", \"description\":\"Japanese dish\"}")).andExpect(
                status().isCreated());

        mockMvc.perform(
                get("/meal/search/findByLabel?label={label}", "Ramen")).andExpect(
                status().isOk()).andExpect(
                jsonPath("$._embedded.meal[0].label").value(
                        "Ramen"));
    }

    @Test
    public void shouldUpdateEntity() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/meal").content(
                "{\"label\": \"Ramen\", \"description\":\"Japanese dish\"}")).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        mockMvc.perform(put(location).content(
                "{\"label\": \"Rāmen\", \"description\":\"Japanese dish !\"}")).andExpect(
                status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.label").value("Rāmen")).andExpect(
                jsonPath("$.description").value("Japanese dish !"));
    }

    @Test
    public void shouldPartiallyUpdateEntity() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/meal").content(
                "{\"label\": \"Ramen\", \"description\":\"Japanese dish\"}")).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");

        mockMvc.perform(
                patch(location).content("{\"label\": \"Ramen Jr.\"}")).andExpect(
                status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
                jsonPath("$.label").value("Ramen Jr.")).andExpect(
                jsonPath("$.description").value("Japanese dish"));
    }

    @Test
    public void shouldDeleteEntity() throws Exception {

        MvcResult mvcResult = mockMvc.perform(post("/meal").content(
                "{\"label\": \"Ramen\", \"description\":\"Japanese dish\"}")).andExpect(
                status().isCreated()).andReturn();

        String location = mvcResult.getResponse().getHeader("Location");
        mockMvc.perform(delete(location)).andExpect(status().isNoContent());

        mockMvc.perform(get(location)).andExpect(status().isNotFound());
    }
}
