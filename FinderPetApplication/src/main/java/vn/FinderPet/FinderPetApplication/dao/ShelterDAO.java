package vn.FinderPet.FinderPetApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.FinderPet.FinderPetApplication.entity.Shelters;

@RepositoryRestResource(path = "shelters")
public interface ShelterDAO extends JpaRepository<Shelters, Long> {

}
