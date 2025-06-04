package lt.techin.bookreservationapp.user_book;

import jakarta.validation.constraints.NotNull;

public record UserBookRequestDTO(@NotNull String title) {
}
