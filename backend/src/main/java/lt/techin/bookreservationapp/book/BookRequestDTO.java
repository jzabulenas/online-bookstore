package lt.techin.bookreservationapp.book;

import jakarta.validation.constraints.NotNull;

record BookRequestDTO(@NotNull String title) {}
