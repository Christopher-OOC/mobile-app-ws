package com.appdeveloperblog.app.io.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="authorities")
public class AuthorityEntity implements Serializable {
	
	private static final long serialVersionUID = -9150140854311803897L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false, length=20)
	private String name;
	
	@ManyToMany(mappedBy="authorities")
	private Collection<RoleEntity> roles;
	
	public AuthorityEntity(String name) {
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
		AuthorityEntity other = (AuthorityEntity) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
