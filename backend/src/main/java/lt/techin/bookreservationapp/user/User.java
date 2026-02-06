package lt.techin.bookreservationapp.user;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import lt.techin.bookreservationapp.role.Role;
import lt.techin.bookreservationapp.user_book.UserBook;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "Users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;
  private String password;
  private boolean isEnabled;
  private String verificationCode;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private List<Role> roles;

  @OneToMany(mappedBy = "user")
  private List<UserBook> books;

  public User(
      String email,
      String password,
      boolean isEnabled,
      String verificationCode,
      List<Role> roles,
      List<UserBook> books) {
    this.email = email;
    this.password = password;
    this.isEnabled = isEnabled;
    this.verificationCode = verificationCode;
    this.roles = roles;
    this.books = books;
  }

  User() {}

  public Long getId() {
    return this.id;
  }

  public String getEmail() {
    return this.email;
  }

  void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  void setPassword(String password) {
    this.password = password;
  }

  public List<Role> getRoles() {
    return this.roles;
  }

  void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  List<UserBook> getBooks() {
    return this.books;
  }

  void setBooks(List<UserBook> books) {
    this.books = books;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isEnabled() {
    return this.isEnabled;
  }

  void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  String getVerificationCode() {
    return this.verificationCode;
  }

  void setVerificationCode(String verificationCode) {
    this.verificationCode = verificationCode;
  }
}
