package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

class LikeBooksTest {

  @Test
  void whenOneBookIsLiked_thenReturn201AndBody() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = this.createUserThenLogInAndGetSession();

    Response generatedBooksResponse =
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
    List<String> generatedBooksList = generatedBooksResponse.jsonPath().getList("result");

    Response likedBookResponse =
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
                    .formatted(generatedBooksList.get(0)))
            .when()
            .post("http://localhost:8080/books")
            .then()
            .statusCode(201)
            .body("id", greaterThan(0))
            .body("$", aMapWithSize(3))
            .extract()
            .response();

    int bookId = likedBookResponse.path("bookId");
    int userId = likedBookResponse.path("userId");
    assertThat(bookId, greaterThan(0));
    assertThat(userId, greaterThan(0));
    assertThat(
        likedBookResponse.getHeader("Location"),
        equalTo("http://localhost:8080/books/" + bookId + "/users/" + userId));
  }

  @Test
  void whenTwoBooksAreLiked_thenReturn201AndBody() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = this.createUserThenLogInAndGetSession();

    Response generatedBooksResponse =
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
    List<String> generatedBooksList = generatedBooksResponse.jsonPath().getList("result");

    Response likedBookResponse =
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
                    .formatted(generatedBooksList.get(0)))
            .when()
            .post("http://localhost:8080/books")
            .then()
            .statusCode(201)
            .body("id", greaterThan(0))
            .body("$", aMapWithSize(3))
            .extract()
            .response();

    int bookId = likedBookResponse.path("bookId");
    int userId = likedBookResponse.path("userId");
    assertThat(bookId, greaterThan(0));
    assertThat(userId, greaterThan(0));
    assertThat(
        likedBookResponse.getHeader("Location"),
        equalTo("http://localhost:8080/books/" + bookId + "/users/" + userId));

    Response likedBookResponse2 =
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
                    .formatted(generatedBooksList.get(1)))
            .when()
            .post("http://localhost:8080/books")
            .then()
            .statusCode(201)
            .body("id", greaterThan(0))
            .body("$", aMapWithSize(3))
            .extract()
            .response();

    int bookId2 = likedBookResponse2.path("bookId");
    int userId2 = likedBookResponse2.path("userId");
    assertThat(bookId2, greaterThan(0));
    assertThat(userId2, greaterThan(0));
    assertThat(
        likedBookResponse2.getHeader("Location"),
        equalTo("http://localhost:8080/books/" + bookId2 + "/users/" + userId2));
  }

  @Test
  void whenThreeBooksAreLiked_thenReturn201AndBody() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = this.createUserThenLogInAndGetSession();

    Response generatedBooksResponse =
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
    List<String> generatedBooksList = generatedBooksResponse.jsonPath().getList("result");

    Response likedBookResponse =
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
                    .formatted(generatedBooksList.get(0)))
            .when()
            .post("http://localhost:8080/books")
            .then()
            .statusCode(201)
            .body("id", greaterThan(0))
            .body("$", aMapWithSize(3))
            .extract()
            .response();

    int bookId = likedBookResponse.path("bookId");
    int userId = likedBookResponse.path("userId");
    assertThat(bookId, greaterThan(0));
    assertThat(userId, greaterThan(0));
    assertThat(
        likedBookResponse.getHeader("Location"),
        equalTo("http://localhost:8080/books/" + bookId + "/users/" + userId));

    Response likedBookResponse2 =
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
                    .formatted(generatedBooksList.get(1)))
            .when()
            .post("http://localhost:8080/books")
            .then()
            .statusCode(201)
            .body("id", greaterThan(0))
            .body("$", aMapWithSize(3))
            .extract()
            .response();

    int bookId2 = likedBookResponse2.path("bookId");
    int userId2 = likedBookResponse2.path("userId");
    assertThat(bookId2, greaterThan(0));
    assertThat(userId2, greaterThan(0));
    assertThat(
        likedBookResponse2.getHeader("Location"),
        equalTo("http://localhost:8080/books/" + bookId2 + "/users/" + userId2));

    Response likedBookResponse3 =
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
                    .formatted(generatedBooksList.get(2)))
            .when()
            .post("http://localhost:8080/books")
            .then()
            .statusCode(201)
            .body("id", greaterThan(0))
            .body("$", aMapWithSize(3))
            .extract()
            .response();

    int bookId3 = likedBookResponse3.path("bookId");
    int userId3 = likedBookResponse3.path("userId");
    assertThat(bookId3, greaterThan(0));
    assertThat(userId3, greaterThan(0));
    assertThat(
        likedBookResponse3.getHeader("Location"),
        equalTo("http://localhost:8080/books/" + bookId3 + "/users/" + userId3));
  }

  @Test
  void whenBookIsAlreadyLikedByOtherUserAndILikeSameBookForNewUser_thenReturn201AndMessage() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = this.createUserThenLogInAndGetSession();
    Response logInResponse2 = this.createUserThenLogInAndGetSession();

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
    Response logInResponse = this.createUserThenLogInAndGetSession();

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

  @Test
  void whenAuthenticatedTriesCallingButNoCSRF_thenReturn403AndBody() {
    Response logInResponse = this.createUserThenLogInAndGetSession();

    given()
        .cookie("JSESSIONID", logInResponse.getSessionId())
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
        .statusCode(403)
        .body("timestamp", containsString(String.valueOf(LocalDateTime.now().getYear())))
        .body("status", equalTo(403))
        .body("error", equalTo("Forbidden"))
        .body("path", equalTo("/books"));
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
