package lt.techin.bookreservationapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MockEmailService implements EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  @Autowired
  public MockEmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
  }

  /**
   * Constructs the HTML content of the email message.
   * 
   * @param entity User who's verification code will be used in the URL for
   *               verification.
   * @return String containing the HTML code.
   */
  private String getVerificationMailContent(User entity) {
    Context context = new Context();

    // TODO: later will probably need to change to environment variable
    String verificationUrl = String
        .format("http://localhost:8080/verify?code=%s", entity.getVerificationCode());

    // Injects the URL to the `applicationUrl` variable inside
    // `user-verify-mock.html` file
    context.setVariable("applicationUrl", verificationUrl);

    return templateEngine.process("user-verify-mock", context);
  }

  /**
   * Creates an email message deliverable, containing text, subject, from and to.
   * 
   * @param entity  User to who's email the message will be addressed.
   * @param content content containing HTML message.
   * @return MimeMessage
   * @throws MessagingException
   */
  private MimeMessage createMessage(User entity, String content) throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
    message.setText(content, true);
    message.setSubject("Welcome to MyApp XYZ");
    message.setFrom("noreply@myapp.xyz");
    message.setTo(entity.getEmail());

    return mimeMessage;
  }

  /**
   * Sends verification email to a user.
   * 
   * @param entity the User to whom a verification email will be sent to.
   */
  @Override
  public void sendVerificationMail(User entity) throws UserMailFailedException {
    String content = getVerificationMailContent(entity);

    try {
      mailSender.send(createMessage(entity, content));
    } catch (MessagingException ex) {
      throw new UserMailFailedException(
          "Could not send e-mail to verify user with e-mail '" + entity.getEmail() + "'", ex);
    }
  }

}
