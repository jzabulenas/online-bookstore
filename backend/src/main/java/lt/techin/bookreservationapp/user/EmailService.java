package lt.techin.bookreservationapp.user;

public interface EmailService {

  void sendVerificationMail(User entity) throws UserMailFailedException;
}
