package lt.techin.bookreservationapp.end_to_end;

import static io.restassured.RestAssured.given;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherSessionListener;

public class TeardownExtension implements LauncherSessionListener {

  @Override
  public void launcherSessionClosed(LauncherSession session) {
    System.out.println("---");
    System.out.println("deleting test database...");
    System.out.println("---");

    String username = System.getenv("MARIADB_USERNAME");
    String password = System.getenv("MARIADB_PASSWORD");

    try (
      Connection conn = DriverManager.getConnection(
        "jdbc:mariadb://localhost:3306/online_bookstore",
        username,
        password
      );
      Statement stmt = conn.createStatement()
    ) {
      stmt.execute("DELETE FROM users_roles");
      stmt.execute("DELETE FROM users_books");
      stmt.execute("DELETE FROM books");
      stmt.execute("DELETE FROM users");
    } catch (Exception e) {
      throw new RuntimeException("Failed to clean up test database", e);
    }

    // Remove existing emails
    given().delete("http://localhost:8025/api/v1/messages");
  }
}
