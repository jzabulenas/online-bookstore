package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class GenerateBooksTest {

  @Test
  void whenBooksAreGenerated_thenReturn200AndListOfBooks() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = createUserThenLogInAndGetSession();

    given()
        .cookie("JSESSIONID", logInResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
    		{
    		  "message": "Dracula by Bram Stoker"
    		}
    		""")
        .when()
        .post("http://localhost:8080/generate-books")
        .then()
        .statusCode(200)
        .body("$", aMapWithSize(1))
        .body("result", hasSize(3));
  }

  @Test
  void whenUserHasBooksTheyLikedBefore_thenNotSeeThemInNewlyGeneratedBooks() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = createUserThenLogInAndGetSession();

    // Generate new books
    Response newlyGeneratedBooksResponse =
        given()
            .cookie("JSESSIONID", logInResponse.getSessionId())
            .cookie("XSRF-TOKEN", csrfToken)
            .header("X-XSRF-TOKEN", csrfToken)
            .contentType(ContentType.JSON)
            .body(
                """
				{
				  "message": "Dracula by Bram Stoker"
				}
				""")
            .when()
            .post("http://localhost:8080/generate-books")
            .then()
            .statusCode(200)
            .body("$", aMapWithSize(1))
            .body("result", hasSize(3))
            .extract()
            .response();
    List<String> newlyGeneratedBooksList = newlyGeneratedBooksResponse.jsonPath().getList("result");

    // Like generated books for user
    given()
        .cookie("JSESSIONID", logInResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
			{
			  "title": "%s"
			}
			"""
                .formatted(newlyGeneratedBooksList.get(0)))
        .when()
        .post("http://localhost:8080/books");

    given()
        .cookie("JSESSIONID", logInResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
			{
			  "title": "%s"
			}
			"""
                .formatted(newlyGeneratedBooksList.get(1)))
        .when()
        .post("http://localhost:8080/books");

    given()
        .cookie("JSESSIONID", logInResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
			{
			  "title": "%s"
			}
			"""
                .formatted(newlyGeneratedBooksList.get(2)))
        .when()
        .post("http://localhost:8080/books");

    // Generate new books, second time
    Response newlyGeneratedBooksResponse2 =
        given()
            .cookie("JSESSIONID", logInResponse.getSessionId())
            .cookie("XSRF-TOKEN", csrfToken)
            .header("X-XSRF-TOKEN", csrfToken)
            .contentType(ContentType.JSON)
            .body(
                """
                {
                  "message": "Dracula by Bram Stoker"
                }
                """)
            .when()
            .post("http://localhost:8080/generate-books")
            .then()
            .statusCode(200)
            .body("$", aMapWithSize(1))
            .body("result", hasSize(3))
            .extract()
            .response();
    List<String> newlyGeneratedBooksList2 =
        newlyGeneratedBooksResponse2.jsonPath().getList("result");

    // Retrieve liked books of user
    Response existingUserBooksResponse =
        given()
            .cookie("JSESSIONID", logInResponse.getSessionId())
            .cookie("XSRF-TOKEN", csrfToken)
            .header("X-XSRF-TOKEN", csrfToken)
            .when()
            .get("/books");
    List<String> existingUserBooksList = existingUserBooksResponse.jsonPath().getList("title");

    assertThat(
        Collections.disjoint(newlyGeneratedBooksList2, existingUserBooksList), equalTo(true));
  }

  @Test
  void whenMessageIsNull_thenReturn400AndMessage() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = createUserThenLogInAndGetSession();

    given()
        .cookie("JSESSIONID", logInResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "message": null
            }
            """)
        .when()
        .post("http://localhost:8080/generate-books")
        .then()
        .statusCode(400)
        .body("message", equalTo("must not be null"))
        .body("$", aMapWithSize(1));
  }

  @Test
  void whenMessageIsTooShort_thenReturn400AndMessage() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = createUserThenLogInAndGetSession();

    given()
        .cookie("JSESSIONID", logInResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "message": "Fe"
            }
            """)
        .when()
        .post("http://localhost:8080/generate-books")
        .then()
        .statusCode(400)
        .body("message", equalTo("size must be between 5 and 100"))
        .body("$", aMapWithSize(1));
  }

  @Test
  void whenMessageIsTooLong_thenReturn400AndMessage() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = createUserThenLogInAndGetSession();

    given()
        .cookie("JSESSIONID", logInResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "message": "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean mj"
            }
            """)
        .when()
        .post("http://localhost:8080/generate-books")
        .then()
        .statusCode(400)
        .body("message", equalTo("size must be between 5 and 100"))
        .body("$", aMapWithSize(1));
  }

  private Response createUserThenLogInAndGetSession() {
    String csrfToken = this.getCsrfToken();
    UUID uuid = UUID.randomUUID();
    String email = "antanas" + uuid;
    String emailForSigningUp = email + "@gmail.com";
    String emailForLoggingIn = email + "%40gmail.com";

    // Send sign up request
    given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "email": "%s",
              "password": "r9$CbHEaGXLUsP",
              "roles": [
                 1
              ]
            }
            """
                .formatted(emailForSigningUp))
        .when()
        .post("http://localhost:8080/signup");

    // Extract verification link from email message
    String verificationSnippet =
        given()
            .when()
            .get("http://localhost:8025/api/v1/messages")
            .then()
            .extract()
            .path(
                "messages.find { msg -> msg.To.any { it.Address == '%s' } }.Snippet"
                    .formatted(emailForSigningUp));

    Pattern pattern = Pattern.compile("(http://localhost:8080/verify\\?code=[^\\s]+)");
    Matcher matcher = pattern.matcher(verificationSnippet);

    // Must call find, otherwise it won't work
    matcher.find();
    String verificationLink = matcher.group(1);

    // Send verification request to extracted link
    given().when().get(verificationLink);

    // Log in
    return given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.URLENC)
        .body("username=%s&password=r9$CbHEaGXLUsP".formatted(emailForLoggingIn))
        .post("http://localhost:8080/login")
        .then()
        .extract()
        .response();
  }

  private String getCsrfToken() {
    Response csrfResponse =
        given().when().get("http://localhost:8080/open").then().extract().response();

    return csrfResponse.cookie("XSRF-TOKEN");
  }
}
