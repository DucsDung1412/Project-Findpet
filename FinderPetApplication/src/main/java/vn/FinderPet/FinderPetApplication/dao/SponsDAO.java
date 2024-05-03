package vn.FinderPet.FinderPetApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.FinderPet.FinderPetApplication.entity.Spons;

@RepositoryRestResource(path = "spons")
public interface SponsDAO extends JpaRepository<Spons, Long> {

}
