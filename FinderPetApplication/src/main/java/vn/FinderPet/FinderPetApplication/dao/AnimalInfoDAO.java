package vn.FinderPet.FinderPetApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.FinderPet.FinderPetApplication.entity.AnimalInfo;

@RepositoryRestResource(path = "animal-info")
public interface AnimalInfoDAO extends JpaRepository<AnimalInfo, Long> {

}
