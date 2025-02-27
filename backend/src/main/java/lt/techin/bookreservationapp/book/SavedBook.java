package lt.techin.bookreservationapp.book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lt.techin.bookreservationapp.entities.User;

@Entity
@Table(
    name = "Saved_books",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "user_id"})})
class SavedBook {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private String title;

  @NotNull @ManyToOne private User user;

  SavedBook(String title, User user) {
    this.title = title;
    this.user = user;
  }

  SavedBook() {}

  String getTitle() {
    return title;
  }

  User getUser() {
    return user;
  }
}
