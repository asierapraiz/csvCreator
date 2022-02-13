package es.anaya.spring.data.s03.simplejpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Technology {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private Integer creationYear;
	
	public Technology () {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCreationYear() {
		return creationYear;
	}

	public void setCreationYear(Integer creationYear) {
		this.creationYear = creationYear;
	}

	@Override
	public String toString() {
		return "Technology [id=" + id + ", name=" + name + ", creationYear=" + creationYear + "]";
	}
	
	
}
