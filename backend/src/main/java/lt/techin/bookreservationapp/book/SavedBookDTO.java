package lt.techin.bookreservationapp.book;

import jakarta.validation.constraints.NotNull;

record SavedBookDTO(@NotNull String title) {}
