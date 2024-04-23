package lt.techin.bookreservationapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lt.techin.bookreservationapp.entities.Book;
import lt.techin.bookreservationapp.repositories.BookRepository;

@Service
public class BookService {

  private final BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  public List<Book> findAllBooks() {
    return bookRepository.findAll();
  }

  public Book findBookById(int id) {
    return bookRepository.findById(id).orElseThrow();
  }

  public boolean existsBookByTitle(String title) {
    return bookRepository.existsByTitle(title);
  }

  public boolean existsBookByIsbn(String isbn) {
    return bookRepository.existsByIsbn(isbn);
  }

  public Book saveBook(Book book) {
    return bookRepository.save(book);
  }

  public boolean existsBookById(int id) {
    return bookRepository.existsById(id);
  }
}
