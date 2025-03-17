package lt.techin.bookreservationapp.book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lt.techin.bookreservationapp.user.User;

@Entity
@Table(
    name = "books",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "user_id"})})
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private String title;

  @NotNull @ManyToOne private User user;

  public Book(String title, User user) {
    this.title = title;
    this.user = user;
  }

  Book() {}

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public User getUser() {
    return user;
  }
}
