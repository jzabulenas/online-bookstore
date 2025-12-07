package lt.techin.bookreservationapp.book;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lt.techin.bookreservationapp.user_book.UserBook;

@Entity
@Table(name = "books")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @OneToMany(mappedBy = "book")
  private List<UserBook> users;

  public Book(String title, List<UserBook> users) {
    this.title = title;
    this.users = users;
  }

  public Book() {
  }

  public Long getId() {
    return this.id;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<UserBook> getUsers() {
    return this.users;
  }

  public void setUsers(List<UserBook> users) {
    this.users = users;
  }

}
