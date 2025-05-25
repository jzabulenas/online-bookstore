package lt.techin.bookreservationapp.book;

import jakarta.validation.constraints.NotNull;

public record BookRequestDTO(@NotNull String title) {
}
