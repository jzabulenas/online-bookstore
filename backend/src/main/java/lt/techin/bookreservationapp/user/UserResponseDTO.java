package lt.techin.bookreservationapp.user;

import java.util.List;

public record UserResponseDTO(long id,
    String email,
    List<Long> roles) {
}
