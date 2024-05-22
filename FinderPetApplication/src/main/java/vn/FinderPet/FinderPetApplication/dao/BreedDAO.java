package vn.FinderPet.FinderPetApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.FinderPet.FinderPetApplication.entity.Breed;

@RepositoryRestResource(path = "breed")
public interface BreedDAO extends JpaRepository<Breed, Long> {

}
