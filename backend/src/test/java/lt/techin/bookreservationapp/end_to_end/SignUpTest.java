package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

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
            .post("http://localhost:8080/signup")
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

    // Extract verification link from email message
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

  @Test
  void whenUserSignsUpWithAsBigPasswordAsPossible_thenReturn201AndBody() {
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
                  "password": "metyjwgaqakvjdrbpqsoywhrqzpesbrtsbtqfseffbivpfsaaihttjnjbmrbexbp",
                  "roles": [
                     1
                  ]
                }
                """
                    .formatted(email))
            .when()
            .post("http://localhost:8080/signup")
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

    // Extract verification link from email message
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

  // Email
  //
  //
  //
  //

  @Test
  void whenEmailIsNull_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "email": null,
              "password": "r9$CbHEaGXLUsP",
              "roles": [
                 1
              ]
            }
            """)
        .when()
        .post("http://localhost:8080/signup")
        .then()
        .statusCode(400)
        .body("$", aMapWithSize(1))
        .body("email", equalTo("must not be null"));
  }

  @Test
  void whenEmailIsTooShort_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "email": "f@b.c",
              "password": "r9$CbHEaGXLUsP",
              "roles": [
                 1
              ]
            }
            """)
        .when()
        .post("http://localhost:8080/signup")
        .then()
        .statusCode(400)
        .body("$", aMapWithSize(1))
        .body("email", equalTo("Email must be at least 7 characters long"));
  }

  @Test
  void whenEmailLocalPartIsTooLong_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
	        {
	          "email": "ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicezrv@gmail.com",
	          "password": "r9$CbHEaGXLUsP",
	          "roles": [
	             1
	          ]
	        }
	        """)
        .when()
        .post("http://localhost:8080/signup")
        .then()
        .statusCode(400)
        .body("$", aMapWithSize(1))
        .body("email", equalTo("must be a well-formed email address"));
  }

  @Test
  void whenEmailDomainPartIsTooLong_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "email": "jurgis@ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicegw.com",
              "password": "r9$CbHEaGXLUsP",
              "roles": [
                 1
              ]
            }
            """)
        .when()
        .post("http://localhost:8080/signup")
        .then()
        .statusCode(400)
        .body("$", aMapWithSize(1))
        .body("email", equalTo("must be a well-formed email address"));
  }

  @Test
  void whenEmailLocalPartAndDomainPartIsTooLong_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "email": "ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicezrrsdfsdfse@ivctsadyhqcfxzjinykxemzadbyajutuqzawknkckrgbzcjlwgufbrcycrdicegwasdasde.com",
              "password": "r9$CbHEaGXLUsP",
              "roles": [
                 1
              ]
            }
            """)
        .when()
        .post("http://localhost:8080/signup")
        .then()
        .statusCode(400)
        .body("$", aMapWithSize(1))
        .body("email", equalTo("must be a well-formed email address"));
  }

  @Test
  void whenEmailAlreadyExists_thenReturn400AndBody() {
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
            .post("http://localhost:8080/signup")
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

    // Extract verification link from email message
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

    // Do not allow second user to register with same email
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
        .post("http://localhost:8080/signup")
        .then()
        .statusCode(400)
        .body("$", aMapWithSize(1))
        .body("email", equalTo("Such email address is already in use"));
  }

  // Password
  //
  //
  //
  //

  @Test
  void whenPasswordIsNull_shouldReturn400AndBody() {
    String csrfToken = this.getCsrfToken();
    UUID uuid = UUID.randomUUID();
    String email = "antanas" + uuid + "@gmail.com";

    given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "email": "%s",
              "password": null,
              "roles": [
                 1
              ]
            }
            """
                .formatted(email))
        .when()
        .post("http://localhost:8080/signup")
        .then()
        .statusCode(400)
        .body("$", aMapWithSize(1))
        .body("password", equalTo("must not be null"));
  }

  @Test
  void whenPasswordIsTooShort_shouldReturn400AndBody() {
    String csrfToken = this.getCsrfToken();
    UUID uuid = UUID.randomUUID();
    String email = "antanas" + uuid + "@gmail.com";

    given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        // The password is 13 characters long
        .body(
            """
            {
              "email": "%s",
              "password": "grxnqdgnsqbqj",
              "roles": [
                 1
              ]
            }
            """
                .formatted(email))
        .when()
        .post("http://localhost:8080/signup")
        .then()
        .statusCode(400)
        .body("$", aMapWithSize(1))
        .body("password", equalTo("size must be between 14 and 64"));
  }

  @Test
  void whenPasswordIsTooLong_shouldReturn400AndBody() {
    String csrfToken = this.getCsrfToken();
    UUID uuid = UUID.randomUUID();
    String email = "antanas" + uuid + "@gmail.com";

    given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        // The provided password is 65 characters long
        .body(
            """
            {
              "email": "%s",
              "password": "metyjwgaqakvjdrbpqsoywhrqzpesbrtsbtqfseffbivpfsaaihttjnjbmrbexbpr",
              "roles": [
                 1
              ]
            }
            """
                .formatted(email))
        .when()
        .post("http://localhost:8080/signup")
        .then()
        .statusCode(400)
        .body("$", aMapWithSize(1))
        .body("password", equalTo("size must be between 14 and 64"));
  }

  @Test
  void whenPasswordIsFoundToBeCompromised_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();
    UUID uuid = UUID.randomUUID();
    String email = "antanas" + uuid + "@gmail.com";

    given()
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "email": "%s",
              "password": "12345678912345",
              "roles": [
                 1
              ]
            }
            """
                .formatted(email))
        .when()
        .post("http://localhost:8080/signup")
        .then()
        .statusCode(400)
        .body("$", aMapWithSize(5))
        .body(
            "detail",
            equalTo(
                "The provided password is compromised and cannot be used. Use something more unique"));
  }

  private String getCsrfToken() {
    Response csrfResponse =
        given().when().get("http://localhost:8080/open").then().extract().response();

    return csrfResponse.cookie("XSRF-TOKEN");
  }
}
