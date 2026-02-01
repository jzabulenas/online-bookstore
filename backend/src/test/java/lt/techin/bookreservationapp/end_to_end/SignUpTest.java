package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SignUpTest {

  @Test
  void whenUserSignsUp_thenReturn201AndBody() {
    String csrfToken = this.getCsrfToken();
    UUID uuid = UUID.randomUUID();
    String email = "antanas" + uuid + "@gmail.com";

    // Sign up
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
                .formatted(email))
        .when()
        .post("/signup")
        .then()
        .statusCode(201)
        .body("$", aMapWithSize(3))
        .body("id", greaterThan(0))
        .body("email", equalTo(email))
        .body("roles", hasSize(1))
        .body("roles[0]", equalTo(1))
        .header("Location", containsString("http://localhost:8080/signup/"));

    // Extract verification link from message
    String verificationSnippet =
        given()
            .when()
            .get("http://localhost:8025/api/v1/messages")
            .then()
            .statusCode(200)
            .extract()
            .path(
                "messages.find { msg -> msg.To.any { it.Address == '%s' } }.Snippet"
                    .formatted(email));

    Pattern pattern = Pattern.compile("(http://localhost:8080/verify\\?code=[^\\s]+)");
    Matcher matcher = pattern.matcher(verificationSnippet);

    Assertions.assertTrue(matcher.find(), "Verification link not found in email snippet");
    String verificationLink = matcher.group(1);

    // Send verification request to extracted link
    given()
        .when()
        .get(verificationLink)
        .then()
        .statusCode(200)
        .body(containsString("Book recommendation app"));
  }

  private String getCsrfToken() {
    Response csrfResponse =
        given().when().get("http://localhost:8080/open").then().extract().response();

    return csrfResponse.cookie("XSRF-TOKEN");
  }
}
