package lt.techin.bookreservationapp.entities;

import java.time.LocalDate;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Books")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  @Pattern(regexp = "^(?!.*\\s{2}).*$", message = "Cannot contain more than one consecutive space")
  @Length(min = 1, max = 255, message = "Must be between 1 and 255 characters long")
  @Column(unique = true)
  private String title;

  @NotEmpty
  @Pattern(
      regexp = "^[A-Z][a-z]+ [A-Z][a-z]+$",
      message =
          "Author's first and last name must start with an uppercase letter, that can be followed by one or more lowercase letters")
  @Length(min = 5, max = 255, message = "Must be between 5 and 255 characters long")
  private String author;

  @ManyToMany
  @JoinTable(
      name = "Books_categories",
      joinColumns = @JoinColumn(name = "Book_id"),
      inverseJoinColumns = @JoinColumn(name = "Category_id"))
  @NotEmpty(message = "Must not be null or empty")
  //  Used for serializing only as an id
  //  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  //  @JsonIdentityReference(alwaysAsId = true)
  private Set<Category> categories;

  @NotEmpty(message = "Cannot be null or empty")
  @Column(length = 2000)
  @Length(min = 1, max = 2000, message = "Must be between 1 and 500 characters long")
  @Pattern(regexp = "^[A-Z].*$", message = "Must start with capital letter")
  @Pattern(regexp = "^(?!.*\\s{2}).*$", message = "Cannot contain more than one consecutive space")
  private String description;

  @NotEmpty
  @Pattern(
      regexp = "^(https?)://[^\\s$]+\\.(jpg|png)$",
      message =
          "URl should start with either \"http://\" or \"https://\" and end with \".jpg\" or \".png")
  private String pictureUrl;

  @Min(value = 1, message = "Pages field must have a value greater than 0")
  private int pages;

  //  @NotEmpty
  //  @Pattern(
  //      regexp = "((978[\\--– ])?[0-9][0-9\\--– ]{10}[\\--– ][0-9xX])|((978)?[0-9]{9}[0-9Xx])",
  //      message = "ISBN is incorrect")
  //  @Column(unique = true)
  private String isbn;

  @NotNull(message = "Publication date field cannot be null")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate publicationDate;

  @NotEmpty
  @Pattern(
      regexp = "^[A-Z][a-z]+$",
      message =
          "Language must start with an uppercase letter, that can be followed by one or more lowercase letters")
  private String language;

  public Book(
      String title,
      String author,
      Set<Category> categories,
      String description,
      String pictureUrl,
      int pages,
      String isbn,
      LocalDate publicationDate,
      String language) {
    this.title = title;
    this.author = author;
    this.categories = categories;
    this.description = description;
    this.pictureUrl = pictureUrl;
    this.pages = pages;
    this.isbn = isbn;
    this.publicationDate = publicationDate;
    this.language = language;
  }

  public Book() {}

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public Set<Category> getCategories() {
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

  public void setCategories(Set<Category> categories) {
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
