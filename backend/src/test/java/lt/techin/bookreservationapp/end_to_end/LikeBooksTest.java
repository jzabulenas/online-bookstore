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

class LikeBooksTest {

  @Test
  void whenBookIsLiked_thenReturn201AndBody() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = createUserThenLogInAndGetSession();

    Response response =
        given()
            .cookie("JSESSIONID", logInResponse.getSessionId())
            .cookie("XSRF-TOKEN", csrfToken)
            .header("X-XSRF-TOKEN", csrfToken)
            .contentType(ContentType.JSON)
            .body(
                """
                {
                  "title": "Dracula by Bram Stoker"
                }
                """)
            .when()
            .post("http://localhost:8080/books")
            .then()
            .statusCode(201)
            .body("id", greaterThan(0))
            .body("$", aMapWithSize(3))
            .extract()
            .response();

    int bookId = response.path("bookId");
    int userId = response.path("userId");
    assertThat(bookId, greaterThan(0));
    assertThat(userId, greaterThan(0));
    assertThat(
        response.getHeader("Location"),
        equalTo("http://localhost:8080/books/" + bookId + "/users/" + userId));
  }

  @Test
  void whenBookIsAlreadyLikedByOtherUserAndILikeSameBookForNewUser_thenReturn201AndMessage() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = createUserThenLogInAndGetSession();
    Response logInResponse2 = createUserThenLogInAndGetSession();

    // First user flow
    Response userResponse =
        given()
            .cookie("JSESSIONID", logInResponse.getSessionId())
            .cookie("XSRF-TOKEN", csrfToken)
            .header("X-XSRF-TOKEN", csrfToken)
            .contentType(ContentType.JSON)
            .body(
                """
                {
                  "title": "Dracula by Bram Stoker"
                }
                """)
            .when()
            .post("http://localhost:8080/books")
            .then()
            .statusCode(201)
            .body("id", greaterThan(0))
            .body("$", aMapWithSize(3))
            .extract()
            .response();

    int bookId = userResponse.path("bookId");
    int userId = userResponse.path("userId");
    assertThat(bookId, greaterThan(0));
    assertThat(userId, greaterThan(0));
    assertThat(
        userResponse.getHeader("Location"),
        equalTo("http://localhost:8080/books/" + bookId + "/users/" + userId));

    // Second user flow
    Response user2Response =
        given()
            .cookie("JSESSIONID", logInResponse2.getSessionId())
            .cookie("XSRF-TOKEN", csrfToken)
            .header("X-XSRF-TOKEN", csrfToken)
            .contentType(ContentType.JSON)
            .body(
                """
                {
                  "title": "Dracula by Bram Stoker"
                }
                """)
            .when()
            .post("http://localhost:8080/books")
            .then()
            .statusCode(201)
            .body("id", greaterThan(0))
            .body("$", aMapWithSize(3))
            .extract()
            .response();

    int bookId2 = user2Response.path("bookId");
    int userId2 = user2Response.path("userId");
    assertThat(bookId2, greaterThan(0));
    assertThat(userId2, greaterThan(0));
    assertThat(
        user2Response.getHeader("Location"),
        equalTo("http://localhost:8080/books/" + bookId2 + "/users/" + userId2));
  }

  @Test
  void whenBookIsLikedForCurrentUserAndITryToLikeItAgain_thenReturn400AndMessage() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = createUserThenLogInAndGetSession();

    // First request
    Response userResponse =
        given()
            .cookie("JSESSIONID", logInResponse.getSessionId())
            .cookie("XSRF-TOKEN", csrfToken)
            .header("X-XSRF-TOKEN", csrfToken)
            .contentType(ContentType.JSON)
            .body(
                """
                {
                  "title": "Dracula by Bram Stoker"
                }
                """)
            .when()
            .post("http://localhost:8080/books")
            .then()
            .statusCode(201)
            .body("id", greaterThan(0))
            .body("$", aMapWithSize(3))
            .extract()
            .response();

    int bookId = userResponse.path("bookId");
    int userId = userResponse.path("userId");
    assertThat(bookId, greaterThan(0));
    assertThat(userId, greaterThan(0));
    assertThat(
        userResponse.getHeader("Location"),
        equalTo("http://localhost:8080/books/" + bookId + "/users/" + userId));

    // Second request
    given()
        .cookie("JSESSIONID", logInResponse.getSessionId())
        .cookie("XSRF-TOKEN", csrfToken)
        .header("X-XSRF-TOKEN", csrfToken)
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "title": "Dracula by Bram Stoker"
            }
            """)
        .when()
        .post("http://localhost:8080/books")
        .then()
        .statusCode(400)
        .body("title", equalTo("Already exists"))
        .body("$", aMapWithSize(1));
  }

  @Test
  void whenUnauthenticatedTriesCalling_thenReturn401() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
            {
              "title": "Dracula by Bram Stoker"
            }
            """)
        .when()
        .post("http://localhost:8080/books")
        .then()
        .statusCode(401)
        .body(emptyOrNullString());
  }

  private String getCsrfToken() {
    Response csrfResponse =
        given().when().get("http://localhost:8080/open").then().extract().response();

    return csrfResponse.cookie("XSRF-TOKEN");
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
    assertThat(matcher.find(), equalTo(true));
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
}
