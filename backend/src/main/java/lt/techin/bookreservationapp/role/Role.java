package lt.techin.bookreservationapp.role;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  Role(String name) {
    this.name = name;
  }

  Role() {}

  Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  void setName(String name) {
    this.name = name;
  }

  @Override
  public String getAuthority() {
    return this.name;
  }
}
