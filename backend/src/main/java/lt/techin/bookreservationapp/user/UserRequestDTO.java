package lt.techin.bookreservationapp.user;

import java.util.List;

public record UserRequestDTO(String email, String password, List<Long> roles) {
}
