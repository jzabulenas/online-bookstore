package lt.techin.bookreservationapp.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private String role;

  // @Transient private Map<String, Object> attributes;

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  // @Override
  // public Map<String, Object> getAttributes() {
  // return this.attributes;
  // }

  // public void setAttributes(Map<String, Object> attributes) {
  // this.attributes = attributes;
  // }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> this.role);
  }

  // @Override
  // public String getName() {
  // return this.email;
  // }

  // Can this be null?
  @Override
  public String getPassword() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getUsername() {
    return this.email;
  }
}
