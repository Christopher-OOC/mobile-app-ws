package com.appdeveloperblog.app.io.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="roles")
public class RoleEntity implements Serializable {

	private static final long serialVersionUID = -9153140754311803897L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false, length=20)
	private String name;
	
	@ManyToMany(mappedBy="roles")
	private Collection<UserEntity> users;
	
	@ManyToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	@JoinTable(name="roles_authorities", joinColumns=@JoinColumn(name="roles_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="authorities_id", referencedColumnName="id"))
	private Collection<AuthorityEntity> authorities;
	
	public RoleEntity(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleEntity other = (RoleEntity) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "RoleEntity [id=" + id + ", name=" + name + "]";
	}
}
