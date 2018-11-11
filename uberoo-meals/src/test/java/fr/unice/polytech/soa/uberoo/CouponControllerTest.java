package fr.unice.polytech.soa.uberoo;

import fr.unice.polytech.soa.uberoo.model.Coupon;
import fr.unice.polytech.soa.uberoo.model.Restaurant;
import fr.unice.polytech.soa.uberoo.repository.CouponRepository;
import fr.unice.polytech.soa.uberoo.repository.MealRepository;
import fr.unice.polytech.soa.uberoo.repository.RestaurantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private MealRepository mealRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	private Coupon coupon;

	@Before
	public void initBeforeTests() {
		couponRepository.deleteAll();
		mealRepository.deleteAll();
		restaurantRepository.deleteAll();

		Restaurant restaurant = new Restaurant("restaurant");
		restaurantRepository.save(restaurant);
		this.coupon = new Coupon("10OFF", restaurant.getId(), 10., Coupon.DiscountType.PERCENT, "10% Off !", new Date(), false);
		couponRepository.save(this.coupon);
	}

	@Test
	public void shouldReturnRepositoryIndex() throws Exception {
		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
				jsonPath("$._links.coupons").exists());
	}

	@Test
	public void shouldReturnCouponsList() throws Exception {
		mockMvc.perform(get("/coupons")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void shouldReturnCouponsListByRestaurant() throws Exception {
		couponRepository.save(new Coupon("AMAZING!50", 5L, 0.5, Coupon.DiscountType.FIXED_CART, "50 (cents) Off !", new Date(), false));
		couponRepository.save(new Coupon("14WOW", 12L, 14., Coupon.DiscountType.FIXED_CART, "14€ Off !", new Date(), false));
		mockMvc.perform(get("/coupons?restaurant=" + coupon.getRestaurantId())).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$._embedded.coupons").exists());
	}

	@Test
	public void shouldRetrieveEntity() throws Exception {
		mockMvc.perform(get("/coupons/" + this.coupon.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.restaurantId").value(this.coupon.getRestaurantId()))
				.andExpect(jsonPath("$.description").value(this.coupon.getDescription()))
				.andExpect(jsonPath("$.amount").value(this.coupon.getAmount()))
				.andExpect(jsonPath("$.date_expires").value(new SimpleDateFormat("yyyy-MM-dd").format(this.coupon.getDate_expires())))
				.andExpect(jsonPath("$.code").value(this.coupon.getCode()))
				.andExpect(jsonPath("$.discountType").value(this.coupon.getDiscountType().name()));
	}

	@Test
	public void shouldCreateEntity() throws Exception {
		String request = "{ \"code\": \"10%OFF\"" +
				", \"restaurantId\": \"14\"" +
				", \"amount\": \"10\"" +
				", \"discountType\": \"PERCENT\"" +
				", \"description\": \"10% Off !\"" +
				", \"date_expires\": \"2018-12-28\"" +
				"}";

		mockMvc.perform(post("/coupons")
				.content(request).contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	public void shoudNotFindUnknownEntity() throws Exception {
		mockMvc.perform(get("/coupons/666")).andExpect(status().isNotFound());
	}
}
