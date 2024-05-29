package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.AuthoritiesDAO;
import vn.finder.pet.dao.UsersDAO;
import vn.finder.pet.entity.Authorities;
import vn.finder.pet.entity.Users;

import java.util.Optional;

@Service
public class UsersService {
    private UsersDAO usersDAO;
    private AuthoritiesDAO authoritiesDAO;

    @Autowired
    public UsersService(UsersDAO usersDAO, AuthoritiesDAO authoritiesDAO) {
        this.usersDAO = usersDAO;
        this.authoritiesDAO = authoritiesDAO;
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

}
