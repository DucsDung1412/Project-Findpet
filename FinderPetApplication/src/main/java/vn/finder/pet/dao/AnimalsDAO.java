package vn.finder.pet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Animals;

import java.util.List;

@RepositoryRestResource(path = "animals")
public interface AnimalsDAO extends JpaRepository<Animals, Long> {
    @Query("SELECT a FROM Animals a WHERE a.breed.breed_type IN :breedType AND a.breed.breed_name LIKE %:breedName% AND a.shelters.shelterAddress LIKE %:shelterAddress% AND a.animalAge IN :ages AND a.animalGender IN :genders AND a.animalSize LIKE %:size% AND a.animalName LIKE %:name% ORDER BY a.animalName DESC")
    Page<Animals> findByBreedNameContains(@Param("breedType") List<String> breedType, @Param("breedName") String breedName, @Param("shelterAddress") String shelterAddress, @Param("ages") List<String> ages, @Param("genders") List<Boolean> genders, @Param("size") String size, @Param("name") String name, Pageable pageable);

    @Query("SELECT a FROM Animals a ORDER BY a.animalName DESC")
    Page<Animals> filterName(Pageable pageable);

    @Query("SELECT a FROM Animals a ORDER BY a.animalDate DESC")
    Page<Animals> filterDate(Pageable pageable);

    @Query("SELECT a " +
            "FROM Animals a " +
            "LEFT JOIN a.listFavorites f " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(f.id) DESC")
    Page<Animals> filterFavorite(Pageable pageable);
}
