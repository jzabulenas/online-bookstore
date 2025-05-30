package lt.techin.bookreservationapp.book;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lt.techin.bookreservationapp.user_book.UserBook;

@Entity
// TODO: remove this
@Table(name = "books",
    uniqueConstraints = { @UniqueConstraint(columnNames = { "title", "user_id" }) })
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @OneToMany
  private List<UserBook> users;

  public Book(String title, List<UserBook> users) {
    this.title = title;
    this.users = users;
  }

  public Book() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<UserBook> getUsers() {
    return users;
  }

  public void setUsers(List<UserBook> users) {
    this.users = users;
  }

  public Long getId() {
    return id;
  }

}
