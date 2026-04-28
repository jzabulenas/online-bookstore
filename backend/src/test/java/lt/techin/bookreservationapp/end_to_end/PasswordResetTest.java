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

class PasswordResetTest {

  // POST /forgot-password
  //
  //
  //
  //

  @Test
  void whenValidEmailIsSubmitted_thenReturn200AndNoBody() {
    String csrfToken = this.getCsrfToken();
    String email = this.createVerifiedUser();

    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "email": "%s"
        }
        """.formatted(email)
      )
      .when()
      .post("http://localhost:8080/forgot-password")
      .then()
      .statusCode(200)
      .body(emptyOrNullString());
  }

  @Test
  void whenUnknownEmailIsSubmitted_thenReturn200AndNoBody() {
    String csrfToken = this.getCsrfToken();

    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "email": "nonexistent@example.com"
        }
        """
      )
      .when()
      .post("http://localhost:8080/forgot-password")
      .then()
      .statusCode(200)
      .body(emptyOrNullString());
  }

  @Test
  void whenForgotPasswordEmailIsNull_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "email": null
        }
        """
      )
      .when()
      .post("http://localhost:8080/forgot-password")
      .then()
      .statusCode(400)
      .body("$", aMapWithSize(1))
      .body("email", equalTo("must not be null"));
  }

  @Test
  void whenForgotPasswordEmailIsTooShort_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "email": "f@b.c"
        }
        """
      )
      .when()
      .post("http://localhost:8080/forgot-password")
      .then()
      .statusCode(400)
      .body("$", aMapWithSize(1))
      .body("email", equalTo("Email must be at least 7 characters long"));
  }

  @Test
  void whenForgotPasswordHasNoCSRF_thenReturn401AndNoBody() {
    given()
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "email": "test@example.com"
        }
        """
      )
      .when()
      .post("http://localhost:8080/forgot-password")
      .then()
      .statusCode(401)
      .body(emptyOrNullString());
  }

  // POST /reset-password
  //
  //
  //
  //

  @Test
  void whenTokenIsValidAndPasswordIsValid_thenReturn200AndNoBody() {
    String csrfToken = this.getCsrfToken();
    String email = this.createVerifiedUser();
    String resetToken = this.initiatePasswordResetAndExtractToken(
      csrfToken,
      email
    );

    // Reset the password
    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "token": "%s",
          "password": "r9$CbHEaGXLUsQ"
        }
        """.formatted(resetToken)
      )
      .when()
      .post("http://localhost:8080/reset-password")
      .then()
      .statusCode(200)
      .body(emptyOrNullString());

    String emailForLoggingIn = email.replace("@", "%40");

    // Verify the new password works
    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.URLENC)
      .body("username=%s&password=r9$CbHEaGXLUsQ".formatted(emailForLoggingIn))
      .post("http://localhost:8080/login")
      .then()
      .statusCode(200)
      .body(emptyOrNullString());

    // Verify the old password no longer works
    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.URLENC)
      .body("username=%s&password=r9$CbHEaGXLUsP".formatted(emailForLoggingIn))
      .post("http://localhost:8080/login")
      .then()
      .statusCode(401)
      .body(emptyOrNullString());
  }

  @Test
  void whenTokenIsUsedTwice_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();
    String email = this.createVerifiedUser();
    String resetToken = this.initiatePasswordResetAndExtractToken(
      csrfToken,
      email
    );

    // Use the token a first time
    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "token": "%s",
          "password": "r9$CbHEaGXLUsQ"
        }
        """.formatted(resetToken)
      )
      .when()
      .post("http://localhost:8080/reset-password")
      .then()
      .statusCode(200);

    // Use the same token a second time
    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "token": "%s",
          "password": "r9$CbHEaGXLUsR"
        }
        """.formatted(resetToken)
      )
      .when()
      .post("http://localhost:8080/reset-password")
      .then()
      .statusCode(400)
      .body("$", aMapWithSize(1))
      .body("token", equalTo("Invalid or expired password reset link"));
  }

  @Test
  void whenTokenIsInvalid_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "token": "00000000000000000000000000000000",
          "password": "r9$CbHEaGXLUsP"
        }
        """
      )
      .when()
      .post("http://localhost:8080/reset-password")
      .then()
      .statusCode(400)
      .body("$", aMapWithSize(1))
      .body("token", equalTo("Invalid or expired password reset link"));
  }

  @Test
  void whenTokenIsNull_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "token": null,
          "password": "r9$CbHEaGXLUsP"
        }
        """
      )
      .when()
      .post("http://localhost:8080/reset-password")
      .then()
      .statusCode(400)
      .body("$", aMapWithSize(1))
      .body("token", equalTo("must not be null"));
  }

  @Test
  void whenNewPasswordIsTooShort_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      // The password is 13 characters long
      .body(
        """
        {
          "token": "00000000000000000000000000000000",
          "password": "grxnqdgnsqbqj"
        }
        """
      )
      .when()
      .post("http://localhost:8080/reset-password")
      .then()
      .statusCode(400)
      .body("$", aMapWithSize(1))
      .body("password", equalTo("size must be between 14 and 64"));
  }

  @Test
  void whenNewPasswordIsTooLong_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      // The password is 65 characters long
      .body(
        """
        {
          "token": "00000000000000000000000000000000",
          "password": "metyjwgaqakvjdrbpqsoywhrqzpesbrtsbtqfseffbivpfsaaihttjnjbmrbexbpr"
        }
        """
      )
      .when()
      .post("http://localhost:8080/reset-password")
      .then()
      .statusCode(400)
      .body("$", aMapWithSize(1))
      .body("password", equalTo("size must be between 14 and 64"));
  }

  @Test
  void whenNewPasswordIsNull_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();

    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "token": "00000000000000000000000000000000",
          "password": null
        }
        """
      )
      .when()
      .post("http://localhost:8080/reset-password")
      .then()
      .statusCode(400)
      .body("$", aMapWithSize(1))
      .body("password", equalTo("must not be null"));
  }

  @Test
  void whenNewPasswordIsCompromised_thenReturn400AndBody() {
    String csrfToken = this.getCsrfToken();
    String email = this.createVerifiedUser();
    String resetToken = this.initiatePasswordResetAndExtractToken(
      csrfToken,
      email
    );

    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "token": "%s",
          "password": "12345678912345"
        }
        """.formatted(resetToken)
      )
      .when()
      .post("http://localhost:8080/reset-password")
      .then()
      .statusCode(400)
      .body("$", aMapWithSize(4))
      .body(
        "detail",
        equalTo(
          "The provided password is compromised and cannot be used. Use something more unique"
        )
      );
  }

  @Test
  void whenResetPasswordHasNoCSRF_thenReturn401AndNoBody() {
    given()
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "token": "00000000000000000000000000000000",
          "password": "r9$CbHEaGXLUsP"
        }
        """
      )
      .when()
      .post("http://localhost:8080/reset-password")
      .then()
      .statusCode(401)
      .body(emptyOrNullString());
  }

  private String getCsrfToken() {
    Response csrfResponse = given()
      .when()
      .get("http://localhost:8080/open")
      .then()
      .extract()
      .response();

    return csrfResponse.cookie("XSRF-TOKEN");
  }

  private String createVerifiedUser() {
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
          "password": "r9$CbHEaGXLUsP",
          "roles": [
             1
          ]
        }
        """.formatted(email)
      )
      .when()
      .post("http://localhost:8080/signup");

    String verificationSnippet = given()
      .when()
      .get("http://localhost:8025/api/v1/messages")
      .then()
      .extract()
      .path(
        "messages.find { msg -> msg.To.any { it.Address == '%s' } }.Snippet".formatted(
          email
        )
      );

    Pattern pattern = Pattern.compile(
      "(http://localhost:8080/verify\\?code=[^\\s]+)"
    );
    Matcher matcher = pattern.matcher(verificationSnippet);

    assertThat(matcher.find(), equalTo(true));

    given().when().get(matcher.group(1));

    return email;
  }

  private String initiatePasswordResetAndExtractToken(
    String csrfToken,
    String email
  ) {
    given()
      .cookie("XSRF-TOKEN", csrfToken)
      .header("X-XSRF-TOKEN", csrfToken)
      .contentType(ContentType.JSON)
      .body(
        """
        {
          "email": "%s"
        }
        """.formatted(email)
      )
      .when()
      .post("http://localhost:8080/forgot-password");

    String messageId = given()
      .when()
      .get("http://localhost:8025/api/v1/messages")
      .then()
      .extract()
      .path(
        "messages.find { msg -> msg.To.any { it.Address == '%s' } && msg.Subject == 'Password Reset Request' }.ID".formatted(
          email
        )
      );

    String html = given()
      .when()
      .get("http://localhost:8025/api/v1/message/" + messageId)
      .then()
      .extract()
      .path("HTML");

    Pattern pattern = Pattern.compile("token=([a-f0-9]{32})");
    Matcher matcher = pattern.matcher(html);

    assertThat(matcher.find(), equalTo(true));

    return matcher.group(1);
  }
}
