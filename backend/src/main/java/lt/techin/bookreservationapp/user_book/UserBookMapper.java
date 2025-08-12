package lt.techin.bookreservationapp.user_book;

public class UserBookMapper {

  public static UserBookResponseDTO toDTO(UserBook userBook) {
    return new UserBookResponseDTO(
        userBook.getId(),
        userBook.getUser().getId(),
        userBook.getBook().getId());
  }
}
