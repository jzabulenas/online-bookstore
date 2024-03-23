package lt.techin.bookreservationapp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lt.techin.bookreservationapp.validation.FirstOrder;
import lt.techin.bookreservationapp.validation.SecondOrder;

@Entity
@Table(name = "Categories")
@GroupSequence({Category.class, FirstOrder.class, SecondOrder.class})
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @NotNull(groups = FirstOrder.class)
  @NotEmpty(groups = SecondOrder.class)
  @Column(unique = true)
  @Size(min = 3, max = 50, message = "Length must be between 3 and 50 characters")
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

  //  public void setName(String name) {
  //    this.name = (name == null) ? null : name.stripTrailing();
  //  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Category{" + "id=" + id + ", name='" + name + '\'' + '}';
  }

  public void setId(int id) {
    this.id = id;
  }
}
