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

  Book() {}

  public Long getId() {
    return this.id;
  }

  public String getTitle() {
    return this.title;
  }

  void setTitle(String title) {
    this.title = title;
  }

  List<UserBook> getUsers() {
    return this.users;
  }

  void setUsers(List<UserBook> users) {
    this.users = users;
  }
}
