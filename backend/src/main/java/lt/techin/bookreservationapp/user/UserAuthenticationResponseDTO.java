package lt.techin.bookreservationapp.user;

import java.util.List;

public record UserAuthenticationResponseDTO(
    String name,
    List<String> authorities) {

}
