package lt.techin.bookreservationapp.user;

import java.util.List;

// Should this be primitive type long?
public record UserResponseDTO(Long id, String email, List<Long> roles) {
}
