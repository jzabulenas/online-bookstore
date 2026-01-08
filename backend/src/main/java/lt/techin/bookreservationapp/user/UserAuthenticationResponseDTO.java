package lt.techin.bookreservationapp.user;

import java.util.List;

record UserAuthenticationResponseDTO(String name, List<String> authorities) {}
