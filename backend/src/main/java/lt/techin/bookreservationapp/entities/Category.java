package lt.techin.bookreservationapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "The field must not be null")
    @NotEmpty(message = "The field must not be empty")
    @Pattern(regexp = "^(?!.*([A-Za-z])\\1\\1)[A-Z][A-Za-z\\s\\W]{2,49}$", message = "Category name must start with an uppercase " +
            "letter, followed by lowercase letters, without numbers, consecutive repeated characters, " +
            "and a length between 5 and 50 characters")

    @Column(unique = true)
    private String name;

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
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
