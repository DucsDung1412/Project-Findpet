package vn.finder.pet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Shelters;

import java.util.List;

@RepositoryRestResource(path = "shelters")
public interface ShelterDAO extends JpaRepository<Shelters, Long> {
    List<Shelters> findByShelterStatus(String status);

    @Query("SELECT s FROM Shelters s WHERE s.shelterStatus NOT LIKE %?1%")
    Page<Shelters> findSheltersByStatusNotContaining(Pageable pageable, String status);;
}
