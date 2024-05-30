package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.AuthoritiesDAO;
import vn.finder.pet.dao.UsersDAO;
import vn.finder.pet.entity.Authorities;
import vn.finder.pet.entity.Users;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsersService {
    private UsersDAO usersDAO;
    private AuthoritiesDAO authoritiesDAO;
    private TwoFactorAuthPasswordsService twoFactorAuthPasswordsService;

    @Autowired
    public UsersService(UsersDAO usersDAO, AuthoritiesDAO authoritiesDAO, TwoFactorAuthPasswordsService twoFactorAuthPasswordsService) {
        this.usersDAO = usersDAO;
        this.authoritiesDAO = authoritiesDAO;
        this.twoFactorAuthPasswordsService = twoFactorAuthPasswordsService;
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
}
