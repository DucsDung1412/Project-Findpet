package vn.finder.pet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Breed;

import java.util.List;

@RepositoryRestResource(path = "breed")
public interface BreedDAO extends JpaRepository<Breed, Long> {
    @Query("SELECT b FROM Breed b WHERE b.breed_type LIKE :breedType")
    List<Breed> findByBreed_type(@Param("breedType") String breedType);
}
