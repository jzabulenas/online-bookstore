package lt.techin.bookreservationapp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lt.techin.bookreservationapp.controllers.CategoryController;
import lt.techin.bookreservationapp.entities.Category;
import lt.techin.bookreservationapp.repositories.CategoryRepository;
import lt.techin.bookreservationapp.services.CategoryService;

@WebMvcTest(controllers = CategoryController.class,
		excludeAutoConfiguration = { SecurityAutoConfiguration.class })
//@WithMockUser
public class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private CategoryService categoryService;
	@MockBean
	private CategoryRepository categoryRepository;

	@Test
	void getCategories_savedCategories_returned() throws Exception {
		given(categoryService.findAll())
				.willReturn(List.of(new Category("Reference"),
						new Category("Engineering & Transportation")));

		mockMvc.perform(get("/categories"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Reference"))
				.andExpect(jsonPath("$[1].name")
						.value("Engineering & Transportation"));

	}

	@Test
	void getCategories_emptyList_returnNotFound() throws Exception {
		given(categoryService.findAll())
				.willReturn(List.of());

		mockMvc.perform(get("/categories"))
				.andExpect(status().isNotFound());
	}

	@Test
	void addCategory_whenSaveCategory_thenReturnIt() throws Exception {
//		given
		Category category = new Category("Health, Fitness & Dieting");
//		BindingResult bindingResult = mock(BindingResult.class);
//		given(ValidationService.processFieldErrors(bindingResult))
//				.willReturn(false);
//		given(bindingResult.hasErrors()).willReturn(false);
		given(categoryService.existsByName(category.getName()))
				.willReturn(false);
		given(categoryService.save(any(Category.class)))
				.willReturn(category);

//		when
		mockMvc.perform(post("/categories")
//		then
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(category))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(
						jsonPath("name").value("Health, Fitness & Dieting"));

		// Verify that the save method was called
		verify(categoryService).save(any(Category.class));
	}
}
