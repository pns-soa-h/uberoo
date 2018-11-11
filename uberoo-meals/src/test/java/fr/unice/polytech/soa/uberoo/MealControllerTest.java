package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Meal;
import fr.unice.polytech.soa.uberoo.model.Restaurant;
import fr.unice.polytech.soa.uberoo.model.Tag;
import fr.unice.polytech.soa.uberoo.repository.MealRepository;
import fr.unice.polytech.soa.uberoo.repository.RestaurantRepository;
import fr.unice.polytech.soa.uberoo.repository.TagRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TagRepository tagRepository;

    private Meal meal;

    @Before
    public void initBeforeTests() {
	    mealRepository.deleteAll();
	    tagRepository.deleteAll();
    	restaurantRepository.deleteAll();
        Tag tag = new Tag("asian");
        Restaurant restaurant = new Restaurant("restaurant");
        tagRepository.save(tag);
        restaurantRepository.save(restaurant);
		this.meal = new Meal("Ramen", "Japanese dish", 7.99, restaurant, "Plat", tag);
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
		mockMvc.perform(get("/meals/" + this.meal.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.label").value(this.meal.getLabel()))
				.andExpect(jsonPath("$.description").value(this.meal.getDescription()))
				.andExpect(jsonPath("$.price").value(this.meal.getPrice()));
    }

    @Test
	public void shoudNotFindUnknownEntity() throws Exception {
	    mockMvc.perform(get("/meals/42")).andExpect(status().isNotFound());
    }
}
