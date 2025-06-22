package lt.techin.bookreservationapp.user;

import java.util.List;

public record UserAuthenticationResponseDTO(long id,
    String email,
    List<String> authorities) {

}
