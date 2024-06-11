package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.FavoritesDAO;
import vn.finder.pet.entity.Favorites;
import vn.finder.pet.entity.Users;

import java.util.Optional;

@Service
public class FavoritesService {
    private FavoritesDAO favoritesDAO;

    @Autowired
    public FavoritesService(FavoritesDAO favoritesDAO, UsersService usersService) {
        this.favoritesDAO = favoritesDAO;
    }

    public Boolean removeOne(Long id) {
        Optional<Favorites> favoriteOptional = this.favoritesDAO.findById(id);
        if (favoriteOptional.isPresent()) {
            this.favoritesDAO.delete(favoriteOptional.get());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean removeAll(Users users) {
        for(int i = 0; i < users.getListFavorites().size(); i++){
            System.out.println(users.getListFavorites().get(i).getId());
            this.favoritesDAO.delete(this.favoritesDAO.findById(users.getListFavorites().get(i).getId()).get());
        }

        return false;
    }
}
