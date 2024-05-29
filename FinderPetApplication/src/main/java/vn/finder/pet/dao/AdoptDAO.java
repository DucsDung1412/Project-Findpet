package vn.finder.pet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Adopt;

@RepositoryRestResource(path = "adopt")
public interface AdoptDAO extends JpaRepository<Adopt, Long> {

}
