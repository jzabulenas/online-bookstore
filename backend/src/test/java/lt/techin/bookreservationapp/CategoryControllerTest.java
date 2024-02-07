package lt.techin.bookreservationapp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lt.techin.bookreservationapp.controllers.CategoryController;
import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.CategoryRepository;
import lt.techin.bookreservationapp.security.SecurityConfig;
import lt.techin.bookreservationapp.services.CategoryService;

@WebMvcTest(controllers = CategoryController.class)
@Import(SecurityConfig.class)
public class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	@MockBean
	private CategoryRepository categoryRepository;

	@Test
	@WithMockUser
	void getCategories_whenAuthenticatedCalls_thenReturnList() throws Exception {
		// given
		given(categoryService.findAll())
			.willReturn(List.of(new Category("Reference"), new Category("Engineering & Transportation")));

		// when
		mockMvc.perform(get("/categories"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].name").value("Reference"))
			.andExpect(jsonPath("$[1].name").value("Engineering & Transportation"));

		// then
		then(categoryService).should().findAll();
	}

	@Test
	@WithMockUser
	void getCategories_whenAuthenticatedCallsEmptyList_thenReturn404() throws Exception {
		given(categoryService.findAll()).willReturn(Collections.emptyList());

		mockMvc.perform(get("/categories"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$").isEmpty());

		then(categoryService).should().findAll();
	}

	@Test
	@WithMockUser(roles = { "ADMIN" })
	void addCategory_whenAdminSavesCategory_then201() throws Exception {
		// given
		Category category = new Category("Health, Fitness & Dieting");
		given(categoryService.existsByName(category.getName())).willReturn(false);
		given(categoryService.save(any(Category.class))).willReturn(category);

		// when
		mockMvc
			.perform(post("/categories").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(category))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("name").value("Health, Fitness & Dieting"));

		// then
		then(categoryService).should().save(any(Category.class));
	}

	@Test
	@WithMockUser
	void addCategory_whenAuthenticatedSavesCategory_then403() throws Exception {
		// given
		Category category = new Category("Health, Fitness & Dieting");
		given(categoryService.existsByName(category.getName())).willReturn(false);
		given(categoryService.save(any(Category.class))).willReturn(category);

		// when
		mockMvc
			.perform(post("/categories").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(category))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden());

		// then
		then(categoryService).should(never()).save(any(Category.class));
	}

	@Test
	@WithMockUser(roles = { "ADMIN" })
	void addCategory_whenAdminSavesAlreadyExistingCategory_thenReturn400() throws Exception {
		Category category = new Category("Crafts, Hobbies & Home");
		given(categoryService.existsByName(anyString())).willReturn(true);

		mockMvc
			.perform(post("/categories").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(category))
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("name").value("Category already exists"));

		then(categoryService).should().existsByName(anyString());
		then(categoryService).should(never()).save(any(Category.class));
	}

}
