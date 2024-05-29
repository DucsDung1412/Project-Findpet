package vn.finder.pet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Breed;

@RepositoryRestResource(path = "breed")
public interface BreedDAO extends JpaRepository<Breed, Long> {

}
