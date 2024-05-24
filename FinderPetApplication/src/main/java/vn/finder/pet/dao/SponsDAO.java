package vn.finder.pet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Spons;

@RepositoryRestResource(path = "spons")
public interface SponsDAO extends JpaRepository<Spons, Long> {

}
