package lt.techin.bookreservationapp.DTOs;

import jakarta.validation.constraints.NotNull;

public record SavedBookDTO(@NotNull String title) {}
