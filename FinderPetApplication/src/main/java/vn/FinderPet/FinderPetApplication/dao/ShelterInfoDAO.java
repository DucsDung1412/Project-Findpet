package vn.FinderPet.FinderPetApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.FinderPet.FinderPetApplication.entity.ShelterInfo;

@RepositoryRestResource(path = "shelter-info")
public interface ShelterInfoDAO extends JpaRepository<ShelterInfo, Long> {

}
