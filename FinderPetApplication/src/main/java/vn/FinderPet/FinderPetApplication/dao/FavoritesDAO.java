package vn.FinderPet.FinderPetApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.FinderPet.FinderPetApplication.entity.Favorites;

@RepositoryRestResource(path = "favorites")
public interface FavoritesDAO extends JpaRepository<Favorites, Long> {

}
