package lt.techin.bookreservationapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Categories")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotEmpty(message = "Cannot be empty")
  @NotNull(message = "Cannot be null")
  @Column(unique = true)
  @Pattern(
      regexp = "^[A-Z][a-z]+$",
      message = "Must start with uppercase letter, all else lowercase")
  @Pattern(
      regexp = "^(?!.*([a-zA-Z])\\1\\1)[a-zA-Z]+$",
      message = "Same letter cannot repeat more than two times")
  @Pattern(regexp = "^[A-Z][a-z]{2,50}$", message = "Length must be between 3 and 50 characters")
  private String name;

  public Category(String name) {
    this.name = name;
  }

  public Category() {}

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = (name == null) ? null : name.stripTrailing();
  }

  @Override
  public String toString() {
    return "Category{" + "id=" + id + ", name='" + name + '\'' + '}';
  }

  public void setId(int id) {
    this.id = id;
  }
}
