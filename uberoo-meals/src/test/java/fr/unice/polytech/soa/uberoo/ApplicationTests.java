package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Meal;
import fr.unice.polytech.soa.uberoo.model.Tag;
import fr.unice.polytech.soa.uberoo.repository.MealRepository;
import org.junit.Before;
import org.junit.Ignore;
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

    private Meal meal;

    @Before
    public void initBeforeTests() {
        mealRepository.deleteAll();
        this.meal = new Meal("Ramen", "Japanese dish", new Tag("asian"));
        mealRepository.save(this.meal);
    }

    @Test
    public void shouldReturnRepositoryIndex() throws Exception {
        mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
                jsonPath("$._links.meals").exists());
    }

    @Test
    public void shouldReturnMealsList() throws Exception {
	    mockMvc.perform(get("/meals")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldReturnMealsListWithTag() throws Exception {
    	mockMvc.perform(get("/meals?tag=asian")).andDo(print()).andExpect(status().isOk())
	        .andExpect(jsonPath("$._embedded.meals").exists());
    }

	@Test
	public void shouldNotReturnMealsListWithUnknownTag() throws Exception {
		mockMvc.perform(get("/meals?tag=french")).andDo(print()).andExpect(status().isOk())
	        .andExpect(jsonPath("$._embedded.meals").doesNotExist());
	}

    @Test
    public void shouldRetrieveEntity() throws Exception {
        mockMvc.perform(get("/meals/"+this.meal.getId())).andExpect(status().isOk()).andExpect(
                jsonPath("$.label").value(this.meal.getLabel())).andExpect(
                jsonPath("$.description").value(this.meal.getDescription()));
    }

    @Test
	public void shoudNotFindUnknownEntity() throws Exception {
	    mockMvc.perform(get("/meals/42")).andExpect(status().isNotFound());
    }
}
