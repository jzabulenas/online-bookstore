package lt.techin.bookreservationapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;
  private final String backendUrl;

  @Autowired
  public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine,
      @Value("${backend.url}") String backendUrl) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
    this.backendUrl = backendUrl;
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

    String verificationUrl = this.backendUrl + "/verify?code=" + entity.getVerificationCode();

    // Injects the URL to the `applicationUrl` variable inside
    // `user-verify.html` file
    context.setVariable("applicationUrl", verificationUrl);

    return this.templateEngine.process("user-verify", context);
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
    MimeMessage mimeMessage = this.mailSender.createMimeMessage();
    MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
    message.setText(content, true);
    message.setSubject("Welcome!");
    message.setFrom(System.getenv("MAIL_FROM"));
    message.setTo(entity.getEmail());

    return mimeMessage;
  }

  /**
   * Sends verification email to a user.
   *
   * @param entity the User to whom a verification email will be sent to.
   */
  public void sendVerificationMail(User entity) throws UserMailFailedException {
    String content = getVerificationMailContent(entity);

    try {
      this.mailSender.send(createMessage(entity, content));
    } catch (MessagingException ex) {
      throw new UserMailFailedException(
          "Could not send e-mail to verify user with e-mail '" + entity.getEmail() + "'", ex);
    }
  }

}
