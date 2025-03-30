package lt.techin.bookreservationapp.user;

import java.util.List;

public record UserResponseDTO(Long id, String email, List<Long> roles) {}
