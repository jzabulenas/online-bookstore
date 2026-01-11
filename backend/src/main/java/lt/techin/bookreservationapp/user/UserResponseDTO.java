package lt.techin.bookreservationapp.user;

import java.util.List;

record UserResponseDTO(long id, String email, List<Long> roles) {}
