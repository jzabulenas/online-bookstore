package lt.techin.bookreservationapp.user_book;

import java.util.List;

class UserBookMapper {

  static UserBookResponseDTO toDTO(UserBook userBook) {
    return new UserBookResponseDTO(
        userBook.getId(), userBook.getUser().getId(), userBook.getBook().getId());
  }

  static List<UserBookTitleResponseDTO> toEntities(List<String> titles) {
    return titles.stream().map(title -> new UserBookTitleResponseDTO(title)).toList();
  }
}
