package lt.techin.bookreservationapp.entities;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Books_categories",
            joinColumns = @JoinColumn(name = "Book_id"),
            inverseJoinColumns = @JoinColumn(name = "Category_id")
    )
    private List<Category> categories;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String pictureUrl;

    private int pages;

    private String Isbn;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate publicationDate;

    private String language;

}
