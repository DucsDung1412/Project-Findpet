package vn.FinderPet.FinderPetApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.FinderPet.FinderPetApplication.entity.Adopt;

@RepositoryRestResource(path = "adopt")
public interface AdoptDAO extends JpaRepository<Adopt, Long> {

}
