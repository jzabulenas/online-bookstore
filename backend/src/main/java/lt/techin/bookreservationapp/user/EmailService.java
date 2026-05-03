package lt.techin.bookreservationapp.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
class EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;
  private final String backendUrl;
  private final String frontendUrl;

  @Autowired
  EmailService(
    JavaMailSender mailSender,
    SpringTemplateEngine templateEngine,
    @Value("${backend.url}") String backendUrl,
    @Value("${FRONTEND_URL}") String frontendUrl
  ) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
    this.backendUrl = backendUrl;
    this.frontendUrl = frontendUrl;
  }

  /**
   * Constructs the HTML content of the email message.
   *
   * @param entity User whose verification code will be used in the URL for verification.
   * @return String containing the HTML code.
   */
  private String getVerificationMailContent(User entity) {
    Context context = new Context();

    String verificationUrl =
      this.backendUrl + "/verify?code=" + entity.getVerificationCode();

    // Injects the URL to the `applicationUrl` variable inside
    // `user-verify.html` file
    context.setVariable("applicationUrl", verificationUrl);

    return this.templateEngine.process("user-verify", context);
  }

  /**
   * Creates an email message deliverable, containing text, subject, from and to.
   *
   * @param entity User to who's email the message will be addressed.
   * @param content content containing HTML message.
   * @param subject the email subject line.
   * @return MimeMessage
   */
  private MimeMessage createMessage(User entity, String content, String subject)
    throws MessagingException {
    MimeMessage mimeMessage = this.mailSender.createMimeMessage();
    MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
    message.setText(content, true);
    message.setSubject(subject);
    message.setFrom(System.getenv("MAIL_FROM"));
    message.setTo(entity.getEmail());

    return mimeMessage;
  }

  /**
   * Sends verification email to a user.
   *
   * @param entity the User to whom a verification email will be sent to.
   */
  void sendVerificationMail(User entity) throws UserMailFailedException {
    String content = this.getVerificationMailContent(entity);

    try {
      this.mailSender.send(this.createMessage(entity, content, "Welcome!"));
    } catch (MessagingException ex) {
      throw new UserMailFailedException(
        "Could not send e-mail to verify user with e-mail '" +
          entity.getEmail() +
          "'",
        ex
      );
    }
  }

  /**
   * Constructs the HTML content of the password reset email.
   *
   * @param entity User whose reset token will be used in the URL.
   * @return String containing the HTML code.
   */
  private String getPasswordResetMailContent(User entity) {
    Context context = new Context();
    String resetUrl =
      this.frontendUrl +
      "/reset-password?token=" +
      entity.getPasswordResetCode();
    context.setVariable("applicationUrl", resetUrl);

    return this.templateEngine.process("password-reset", context);
  }

  /**
   * Sends a password reset email to a user.
   *
   * @param entity the User to whom the password reset email will be sent.
   */
  void sendPasswordResetMail(User entity) throws UserMailFailedException {
    String content = this.getPasswordResetMailContent(entity);

    try {
      this.mailSender.send(
        this.createMessage(entity, content, "Password Reset Request")
      );
    } catch (MessagingException ex) {
      throw new UserMailFailedException(
        "Could not send password reset e-mail to '" + entity.getEmail() + "'",
        ex
      );
    }
  }
}
