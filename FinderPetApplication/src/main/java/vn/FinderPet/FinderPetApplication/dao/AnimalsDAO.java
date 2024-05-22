package vn.FinderPet.FinderPetApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.FinderPet.FinderPetApplication.entity.Animals;

@RepositoryRestResource(path = "animals")
public interface AnimalsDAO extends JpaRepository<Animals, Long> {

}
