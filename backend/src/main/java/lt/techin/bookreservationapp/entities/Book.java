package lt.techin.bookreservationapp.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "The title field must not be null")
    @NotEmpty(message = "The title field must not be empty")
    private String title;

    @NotNull(message = "The author field must not be null")
    @NotEmpty(message = "The author field must not be empty")
    private String author;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Books_categories",
            joinColumns = @JoinColumn(name = "Book_id"),
            inverseJoinColumns = @JoinColumn(name = "Category_id")
    )
    private List<Category> categories;

    @NotNull(message = "The description field must not be null")
    @NotEmpty(message = "The description field must not be empty")
    @Column(columnDefinition = "CLOB")
    private String description;

    @NotNull(message = "The picture url field must not be null")
    @NotEmpty(message = "The picture url field must not be empty")
    private String pictureUrl;

    @Min(value = 1, message = "Pages field must have a value greater than 0")
    private int pages;

    @NotNull(message = "The ISBN field must not be null")
    @NotEmpty(message = "The ISBN field must not be empty")
    private String Isbn;

    @NotNull(message = "Publication date field cannot be null")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate publicationDate;

    @NotNull(message = "The language field must not be null")
    @NotEmpty(message = "The language field must not be empty")
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
        return Isbn;
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
        Isbn = isbn;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
