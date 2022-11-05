package tcbs.com.cpm.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "`group`")
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private int departmentId;

  private String name;

  @ManyToMany
  @JoinTable(
    name = "group_role",
    joinColumns = @JoinColumn(name = "group_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
  Set<Role> roles;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "group_user",
    joinColumns = @JoinColumn(name = "group_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  Set<User> users;
}
