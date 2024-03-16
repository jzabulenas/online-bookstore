package lt.techin.bookreservationapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

  @Autowired private MockMvc mockMvc;

  @MockBean private CategoryService categoryService;

  @MockBean private CategoryRepository categoryRepository;

  // getCategories

  @Test
  @WithMockUser
  void getCategories_whenAuthenticatedCalls_thenReturnList() throws Exception {
    // given
    given(categoryService.findAll())
        .willReturn(
            List.of(new Category("Reference"), new Category("Engineering & Transportation")));

    // when
    mockMvc
        .perform(get("/categories"))
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

    mockMvc
        .perform(get("/categories"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());

    then(categoryService).should().findAll();
  }

  @Test
  void getCategories_whenUnauthenticatedCalls_thenReturn401() throws Exception {
    // given
    given(categoryService.findAll())
        .willReturn(
            List.of(new Category("Reference"), new Category("Engineering & Transportation")));

    // when
    mockMvc.perform(get("/categories")).andExpect(status().isUnauthorized());

    // then
    then(categoryService).should(never()).findAll();
  }

  // getCategory

  @Test
  @WithMockUser
  void getCategory_whenAuthenticatedAndCategoryDoesNotExist_thenReturn404() throws Exception {
    given(categoryService.existsCategoryById(anyInt())).willReturn(false);

    mockMvc
        .perform(get("/categories/{id}", 1))
        .andExpect(status().isNotFound())
        .andExpect(content().string(""));

    then(categoryService).should().existsCategoryById(anyInt());
    then(categoryService).should(never()).findById(anyInt());
  }

  @Test
  @WithMockUser
  void getCategory_whenAuthenticatedCategoryExists_thenReturn200() throws Exception {
    given(categoryService.existsCategoryById(anyInt())).willReturn(true);
    given(categoryService.findById(anyInt())).willReturn(new Category("Sports & Outdoors"));

    mockMvc
        .perform(get("/categories/{id}", 6))
        .andExpect(status().isOk())
        .andExpect(jsonPath("name").value("Sports & Outdoors"));

    then(categoryService).should().existsCategoryById(anyInt());
    then(categoryService).should().findById(anyInt());
  }

  @Test
  void getCategory_whenUnauthenticatedCalls_thenReturn401() throws Exception {
    given(categoryService.existsCategoryById(anyInt())).willReturn(true);
    given(categoryService.findById(anyInt())).willReturn(new Category("Sports & Outdoors"));

    mockMvc.perform(get("/categories/{id}", 6)).andExpect(status().isUnauthorized());

    then(categoryService).should(never()).existsCategoryById(anyInt());
    then(categoryService).should(never()).findById(anyInt());
  }

  // addCategory

  @Test
  @WithMockUser(roles = {"ADMIN"})
  void addCategory_whenAdminSavesCategory_then201() throws Exception {
    // given
    Category category = new Category("Health, Fitness & Dieting");
    given(categoryService.existsByName(category.getName())).willReturn(false);
    given(categoryService.save(any(Category.class))).willReturn(category);

    // when
    mockMvc
        .perform(
            post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(category))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("name").value("Health, Fitness & Dieting"));

    // then
    then(categoryService).should().save(any(Category.class));
  }

  @Test
  @WithMockUser(roles = {"ADMIN"})
  void addCategory_whenAdminSavesAlreadyExistingCategory_thenReturn400() throws Exception {
    Category category = new Category("Crafts, Hobbies & Home");
    given(categoryService.existsByName(anyString())).willReturn(true);

    mockMvc
        .perform(
            post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(category))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("name").value("Category already exists"));

    then(categoryService).should().existsByName(anyString());
    then(categoryService).should(never()).save(any(Category.class));
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
        .perform(
            post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(category))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());

    // then
    then(categoryService).should(never()).save(any(Category.class));
  }

  // Do I need to do this? The test earlier might suffice.
  @Test
  @WithMockUser
  void addCategory_whenAuthenticatedSavesAlreadyExistingCategory_thenReturn403() throws Exception {
    Category category = new Category("Crafts, Hobbies & Home");
    given(categoryService.existsByName(anyString())).willReturn(true);

    mockMvc
        .perform(
            post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(category))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());

    then(categoryService).should(never()).existsByName(anyString());
    then(categoryService).should(never()).save(any(Category.class));
  }

  @Test
  void addCategory_whenUnauthenticatedSavesCategory_then401() throws Exception {
    // given
    Category category = new Category("Health, Fitness & Dieting");
    given(categoryService.existsByName(category.getName())).willReturn(false);
    given(categoryService.save(any(Category.class))).willReturn(category);

    // when
    mockMvc
        .perform(
            post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(category))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());

    // then
    then(categoryService).should(never()).existsByName(anyString());
    then(categoryService).should(never()).save(any(Category.class));
  }

  // updateCategory

  @Test
  @WithMockUser(roles = {"ADMIN"})
  void updateCategory_whenAdminProvidesAlreadyExistingCategory_thenReturn400() throws Exception {
    given(categoryService.existsByName(anyString())).willReturn(true);

    mockMvc
        .perform(
            put("/categories/{id}", 370)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                {
                	"name": "Crafts, Hobbies & Home"
                }
                """)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("name").value("Category already exists"));

    then(categoryService).should().existsByName(anyString());
    then(categoryService).should(never()).save(any(Category.class));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void updateCategory_whenAdminFindsCategory_thenReturnUpdatedCategory() throws Exception {
    // Given
    int categoryId = 1;
    Category existingCategory = new Category("Crafts, Hobbies & Home");

    String newCategoryName = "Engineering & Transportation";
    existingCategory.setId(categoryId);

    given(categoryService.findById(categoryId)).willReturn(existingCategory);
    given(categoryService.existsByName(newCategoryName)).willReturn(false);
    given(categoryService.save(any(Category.class))).willReturn(existingCategory);

    // When
    mockMvc
        .perform(
            put("/categories/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"" + newCategoryName + "\"}")
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(categoryId))
        .andExpect(jsonPath("$.name").value(newCategoryName));

    // Then
    then(categoryService).should().findById(categoryId);
    then(categoryService).should().existsByName(newCategoryName);
    then(categoryService).should().save(existingCategory);
    assertThat(newCategoryName).isEqualTo(existingCategory.getName());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void updateCategory_whenAdminPutsNonExistentId_then201AndReturnBody() throws Exception {
    String categoryName = "Engineering & Transportation";

    // I do not need to stub other statements, as the default are null and false
    // already
    // I also used specific id and name for then, as may be better?
    given(categoryService.save(any(Category.class))).willReturn(new Category(categoryName));

    mockMvc
        .perform(
            put("/categories/{id}", 45)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                	{
                		"name": "%s"
                	}
                    """
                        .formatted(categoryName))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(0))
        .andExpect(jsonPath("name").value(categoryName));

    then(categoryService).should().findById(45);
    then(categoryService).should().existsByName(categoryName);
    then(categoryService).should().save(any(Category.class));
  }

  @Test
  @WithMockUser
  void updateCategory_whenAuthenticatedTriesPut_thenReturn403() throws Exception {
    mockMvc
        .perform(
            put("/categories/{id}", 413)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                {
                	"name": "Crafts, Hobbies & Home"
                }
                """)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());

    then(categoryService).should(never()).findById(anyInt());
    then(categoryService).should(never()).existsByName(anyString());
    then(categoryService).should(never()).save(any(Category.class));
  }

  @Test
  void updateCategory_whenUnauthenticatedTriesPut_thenReturn401() throws Exception {
    mockMvc
        .perform(
            put("/categories/{id}", 413)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                {
                	"name": "Crafts, Hobbies & Home"
                }
                """)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());

    then(categoryService).should(never()).findById(anyInt());
    then(categoryService).should(never()).existsByName(anyString());
    then(categoryService).should(never()).save(any(Category.class));
  }

  // deleteCategory

  @Test
  @WithMockUser(roles = "ADMIN")
  void deleteCategory_whenAdminDeletesCategory_thenReturn200() throws Exception {
    int id = 50;
    given(categoryService.existsCategoryById(id)).willReturn(true);

    mockMvc
        .perform(
            delete("/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));

    then(categoryService).should().existsCategoryById(id);
    then(categoryService).should().deleteCategoryById(id);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void deleteCategory_whenAdminDeletesNonExistentCategory_thenReturn404() throws Exception {
    int id = 50;
    given(categoryService.existsCategoryById(id)).willReturn(false);

    mockMvc
        .perform(
            delete("/categories/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().string(""));

    then(categoryService).should().existsCategoryById(id);
    then(categoryService).should(never()).deleteCategoryById(id);
  }

  @Test
  @WithMockUser
  void deleteCategory_whenAuthenticatedTriesDeleteCategory_thenReturn403() throws Exception {
    given(categoryService.existsCategoryById(anyInt())).willReturn(true);

    mockMvc
        .perform(
            delete("/categories/{id}", 93)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());

    then(categoryService).should(never()).existsCategoryById(anyInt());
    then(categoryService).should(never()).deleteCategoryById(anyInt());
  }

  @Test
  void deleteCategory_whenUnauthenticatedTriesDeleteCategory_thenReturn401() throws Exception {
    // I don't think I actually need to provide given, as it will b?
    given(categoryService.existsCategoryById(anyInt())).willReturn(true);

    mockMvc
        .perform(
            delete("/categories/{id}", 93)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());

    then(categoryService).should(never()).existsCategoryById(anyInt());
    then(categoryService).should(never()).deleteCategoryById(anyInt());
  }
}
