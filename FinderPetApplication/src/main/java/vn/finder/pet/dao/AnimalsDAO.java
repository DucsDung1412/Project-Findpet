package vn.finder.pet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Adopt;
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

    @Query("SELECT COUNT(a.id) FROM Animals a LEFT JOIN a.listAdopt ad WHERE ad.id IS NULL AND a.shelters.id = :id")
    Integer findCountAnimalsAvailable(@Param("id") Long id);

    @Query("SELECT COUNT(a.id) FROM Animals a INNER JOIN a.listAdopt ad WHERE ad.adopt_status LIKE '%Awaiting%' AND a.shelters.id = :id")
    Integer findCountAwaitingAnimals(@Param("id") Long id);

    @Query("SELECT COUNT(a.id) FROM Animals a JOIN a.listAdopt ad WHERE ad.adopt_status LIKE '%Adopted%' AND a.shelters.id = :id")
    Integer findCountAnimalsAdopted(@Param("id") Long id);

    @Query("SELECT COUNT(a.id) FROM Animals a WHERE a.shelters.id = :id")
    Integer findCountAnimalsInShelter(@Param("id") Long id);

    @Query("SELECT COUNT(a.id) FROM Animals a INNER JOIN a.listAdopt ad WHERE a.shelters.id = :id")
    Integer findCountAdopt(@Param("id") Long id);

    @Query("SELECT ad FROM Animals a INNER JOIN a.listAdopt ad WHERE ad.adopt_status LIKE %:status% AND a.shelters.id = :id")
    Page<Adopt> findByStatus(@Param("status") String status, Pageable pageable, @Param("id") Long id);

    @Query("SELECT COUNT(a.id) FROM Animals a INNER JOIN a.listFavorites f WHERE a.shelters.id = :id")
    Integer findCountFavorite(@Param("id") Long id);

    @Query("SELECT a FROM Animals a WHERE a.shelters.id = :id")
    Page<Animals> findAllPet(Pageable pageable, @Param("id") Long id);
}
