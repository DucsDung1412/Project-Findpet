package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.AuthoritiesDAO;
import vn.finder.pet.dao.UsersDAO;
import vn.finder.pet.entity.Authorities;
import vn.finder.pet.entity.Favorites;
import vn.finder.pet.entity.Users;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsersService {
    private UsersDAO usersDAO;
    private AuthoritiesDAO authoritiesDAO;
    private TwoFactorAuthPasswordsService twoFactorAuthPasswordsService;
    private FavoritesService favoritesService;

    @Autowired
    public UsersService(UsersDAO usersDAO, AuthoritiesDAO authoritiesDAO, TwoFactorAuthPasswordsService twoFactorAuthPasswordsService, @Lazy FavoritesService favoritesService) {
        this.usersDAO = usersDAO;
        this.authoritiesDAO = authoritiesDAO;
        this.twoFactorAuthPasswordsService = twoFactorAuthPasswordsService;
        this.favoritesService = favoritesService;
    }

    @Transactional
    public Users createdUser(Users users){
        Authorities authorities = new Authorities(users, "ROLE_USER");
        authorities.setId(null);
        this.authoritiesDAO.save(authorities);
        return this.usersDAO.save(users);
    }

    @Transactional
    public Optional<Users> findById(String username){
        return this.usersDAO.findById(username);
    }

    public boolean isValidEmailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        if (this.twoFactorAuthPasswordsService.validatePatternPassword(password)) {
            return true;
        }
        return false;
    }

    public boolean isUserExists(String userName) {
        if (!this.usersDAO.findById(userName).isEmpty()){
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean changeInfoUser(Users user) {
        if(!this.usersDAO.findById(user.getUserName()).isEmpty()){
            this.usersDAO.save(user);
            return true;
        }
        return false;
    }

    public boolean deleteFavorite(Long id) {
        if(this.favoritesService.removeOne(id)){
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteAllFavorite(String email) {
        Optional<Users> usersOptional = this.usersDAO.findById(email);
        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            List<Favorites> favorites = user.getListFavorites();

            // Sử dụng Iterator để tránh ConcurrentModificationException
            Iterator<Favorites> iterator = favorites.iterator();
            while (iterator.hasNext()) {
                Favorites favorite = iterator.next();
                this.favoritesService.removeOne(favorite.getId());
                iterator.remove(); // Xóa khỏi danh sách của user (nếu cần)
            }
            return true;
        }
        return false;
    }
}
