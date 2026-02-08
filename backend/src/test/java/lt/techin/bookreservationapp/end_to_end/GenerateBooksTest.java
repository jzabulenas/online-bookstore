package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

class GenerateBooksTest {

  @Test
  void whenBooksAreGenerated_thenReturn200AndListOfBooks() throws JsonProcessingException {
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
        .post("/generate-books")
        .then()
        .statusCode(200)
        .body("$", aMapWithSize(1))
        .body("result", hasSize(3));
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
        .post("/signup");

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
        .post("/login")
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
