package lt.techin.bookreservationapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Saved_books")
public class SavedBook {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private String title;

  @NotNull @ManyToOne private User user;

  public SavedBook(String title, User user) {
    this.title = title;
    this.user = user;
  }

  public String getTitle() {
    return title;
  }

  public User getUser() {
    return user;
  }
}
