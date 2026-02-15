package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

class SavedBooksTest {

  @Test
  void whenCalled_thenReturnBooksAnd200() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = this.createUserThenLogInAndGetSession();

    // Generate books
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

    // Like a book
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

    // Like a second book
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

    // Get saved books
    given()
        .cookie("JSESSIONID", logInResponse.getSessionId())
        .when()
        .get("/books")
        .then()
        .statusCode(200)
        .body("$", hasSize(2))
        .body("[0].title", equalTo(generatedBooksList.get(0)))
        .body("[0]", aMapWithSize(1))
        .body("[1].title", equalTo(generatedBooksList.get(1)))
        .body("[1]", aMapWithSize(1));
  }

  @Test
  void whenOneUserHasBooksTheySaved_thenOtherUserHasNoneAnd200() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = this.createUserThenLogInAndGetSession();

    // Generate books
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

    // Like a book
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

    // Check saved books of other user
    Response logInResponse2 = this.createUserThenLogInAndGetSession();

    given()
        .cookie("JSESSIONID", logInResponse2.getSessionId())
        .when()
        .get("http://localhost:8080/books")
        .then()
        .statusCode(200)
        .body("$", empty());
  }

  @Test
  void whenNoSavedBooksExist_thenReturnEmptyListAnd200() {
    String csrfToken = this.getCsrfToken();
    Response logInResponse = this.createUserThenLogInAndGetSession();

    // Generate books
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

    // Check that no books are saved
    given()
        .cookie("JSESSIONID", logInResponse.getSessionId())
        .when()
        .get("http://localhost:8080/books")
        .then()
        .statusCode(200)
        .body("$", empty());
  }

  @Test
  void whenUnauthenticated_thenReturn401AndNoBody() {
    given()
        .when()
        .get("http://localhost:8080/books")
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
