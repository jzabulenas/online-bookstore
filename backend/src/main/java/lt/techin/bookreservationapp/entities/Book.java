package lt.techin.bookreservationapp.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "The title field must not be null")
    @NotEmpty(message = "The title field must not be empty")
    @Pattern(regexp = "^[A-Z0-9][a-zA-Z0-9 .,:'\"!?&()-]+$", message = "Book title must start with an uppercase " +
            "letter, that can be followed by a mix of alphanumeric characters, spaces, and certain punctuation marks")
    @Column(unique = true)
    private String title;

    @NotNull(message = "The author field must not be null")
    @NotEmpty(message = "The author field must not be empty")
    @Pattern(regexp = "^[A-Z][a-z]+ [A-Z][a-z]+$", message = "Author's first and last name must start with " +
            "an uppercase letter, that can be followed by one or more lowercase letters")
    private String author;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Books_categories",
            joinColumns = @JoinColumn(name = "Book_id"),
            inverseJoinColumns = @JoinColumn(name = "Category_id")
    )
    @NotEmpty(message = "The categories field must not be empty")
    @NotNull(message = "The categories field must not be null")
    private List<Category> categories;

    @NotNull(message = "The description field must not be null")
    @NotEmpty(message = "The description field must not be empty")
    @Column(columnDefinition = "CLOB")
    @Pattern(regexp = "^[A-Z].{0,299}$", message = "Description should start with a capital letter " +
            "and is limited to a maximum of 300 characters")
    private String description;

    @NotNull(message = "The picture url field must not be null")
    @NotEmpty(message = "The picture url field must not be empty")
        @Pattern(regexp = "^(https?)://[^\\s$]+\\.(jpg|png)$", message = "URl should start with either " +
                "\"http://\" or \"https://\" and end with \".jpg\" or \".png")
    private String pictureUrl;

    @Min(value = 1, message = "Pages field must have a value greater than 0")
    private int pages;

    @NotNull(message = "The ISBN field must not be null")
    @NotEmpty(message = "The ISBN field must not be empty")
    @Pattern(regexp = "((978[\\--– ])?[0-9][0-9\\--– ]{10}[\\--– ][0-9xX])|((978)?[0-9]{9}[0-9Xx])", message = "ISBN " +
            "is incorrect")
    @Column(unique = true)
    private String isbn;

    @NotNull(message = "Publication date field cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;

    @NotNull(message = "The language field must not be null")
    @NotEmpty(message = "The language field must not be empty")
    @Pattern(regexp = "^[A-Z][a-z]+$", message = "Language must start with an uppercase " +
            "letter, that can be followed by one or more lowercase letters")
    private String language;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public int getPages() {
        return pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
