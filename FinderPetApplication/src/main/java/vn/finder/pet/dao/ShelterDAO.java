package vn.finder.pet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Shelters;

@RepositoryRestResource(path = "shelters")
public interface ShelterDAO extends JpaRepository<Shelters, Long> {

}
