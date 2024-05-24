package vn.finder.pet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Favorites;

@RepositoryRestResource(path = "favorites")
public interface FavoritesDAO extends JpaRepository<Favorites, Long> {

}
