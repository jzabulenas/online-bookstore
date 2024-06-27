package lt.techin.bookreservationapp.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Categories")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Size(min = 3, max = 50, message = "Length must be between 3 and 50 characters")
  @Column(unique = true)
  private String name;

  public Category(String name) {
    this.name = name;
  }

  public Category() {}

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  //  public void setName(String name) {
  //    this.name = (name == null) ? null : name.stripTrailing();
  //  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Category{" + "id=" + id + ", name='" + name + '\'' + '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Category other = (Category) obj;
    return Objects.equals(id, other.id) && Objects.equals(name, other.name);
  }
}
