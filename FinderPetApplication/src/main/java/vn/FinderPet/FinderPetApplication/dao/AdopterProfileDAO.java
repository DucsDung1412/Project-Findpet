package vn.FinderPet.FinderPetApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.FinderPet.FinderPetApplication.entity.AdopterProfile;

@RepositoryRestResource(path = "adopter-profile")
public interface AdopterProfileDAO extends JpaRepository<AdopterProfile, Long> {

}
