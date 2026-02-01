package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class SignUpTest {

  @Test
  void whenUserSignsUp_thenReturn201AndBody() {
    String csrfToken = this.getCsrfToken();
    UUID uuid = UUID.randomUUID();
    String email = "antanas" + uuid + "@gmail.com";

    // Send sign up request
    Response response =
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
            .body("email", equalTo(email))
            .body("roles", hasSize(1))
            .body("roles[0]", equalTo(1))
            .extract()
            .response();

    int id = response.path("id");
    assertThat(id, greaterThan(0));
    assertThat(response.getHeader("Location"), equalTo("http://localhost:8080/signup/" + id));

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

    assertThat(matcher.find(), equalTo(true));
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
