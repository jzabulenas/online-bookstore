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

    @Column(columnDefinition = "CLOB")
    private String description;

    private String pictureUrl;

    private int pages;

    private String Isbn;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate publicationDate;

    private String language;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
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
