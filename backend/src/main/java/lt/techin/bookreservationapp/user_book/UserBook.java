package lt.techin.bookreservationapp.user_book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lt.techin.bookreservationapp.book.Book;
import lt.techin.bookreservationapp.user.User;

@Entity
@Table(name = "users_books")
public class UserBook {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne
  private User user;

  @NotNull
  @ManyToOne
  private Book book;

  public UserBook(User user, Book book) {
    this.user = user;
    this.book = book;
  }

  public UserBook() {
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

}
