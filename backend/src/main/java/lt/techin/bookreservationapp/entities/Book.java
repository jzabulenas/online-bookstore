package lt.techin.bookreservationapp.entities;
import java.time.LocalDate;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    //Place for Category column

    @Column(columnDefinition = "TEXT")
    private String description;

    private String pictureUrl;

    private int pages;

    private String Isbn;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
//    @Column(name = "date")
    private LocalDate publicationDate;

    private String language;

}
