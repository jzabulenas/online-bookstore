// package lt.techin.bookreservationapp.end_to_end;
//
// import static io.restassured.RestAssured.given;
// import static org.hamcrest.Matchers.equalTo;
//
// import java.util.Base64;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.test.context.ActiveProfiles;
//
// import io.restassured.RestAssured;
// import io.restassured.http.ContentType;
// import lt.techin.bookreservationapp.entities.Category;
// import lt.techin.bookreservationapp.services.CategoryService;
//
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @ActiveProfiles("test")
// public class CategoryControllerTest {
//
//  @Autowired private CategoryService categoryService;
//
//  @LocalServerPort private int port;
//
//  @BeforeEach
//  void setUp() {
//    RestAssured.port = port;
//    categoryService.deleteAllCategories();
//  }
//
//  // getCategories
//
//  @Test
//  void getCategories_whenCall_thenReturnListAnd200() {
//    Category category1 = new Category("Engineering & Transportation");
//    Category category2 = new Category("Reference");
//    categoryService.saveCategory(category1);
//    categoryService.saveCategory(category2);
//
//    given()
//        .contentType(ContentType.JSON)
//        .header(
//            "Authorization",
//            "Basic " + Base64.getEncoder().encodeToString("tony:soprano".getBytes()))
//        .when()
//        .get("/categories")
//        .then()
//        .statusCode(200)
//        .body("size()", equalTo(2))
//        .body("[0].size()", equalTo(2))
//        .body("[1].size()", equalTo(2))
//        .body("[0].id", equalTo(category1.getId().intValue()))
//        .body("[0].name", equalTo(category1.getName()))
//        .body("[1].id", equalTo(category2.getId().intValue()))
//        .body("[1].name", equalTo(category2.getName()));
//  }
//
//  @Test
//  void getCategories_whenCallAndEmptyList_thenReturnEmptyListAnd200() {
//    given()
//        .contentType(ContentType.JSON)
//        .header(
//            "Authorization",
//            "Basic " + Base64.getEncoder().encodeToString("tony:soprano".getBytes()))
//        .when()
//        .get("/categories")
//        .then()
//        .statusCode(200)
//        .body("size()", equalTo(0));
//  }
//
//  @Test
//  void getCategories_whenUnauthenticatedCalls_thenReturn401() {
//    given().contentType(ContentType.JSON).when().get("/categories").then().statusCode(401);
//  }
// }
