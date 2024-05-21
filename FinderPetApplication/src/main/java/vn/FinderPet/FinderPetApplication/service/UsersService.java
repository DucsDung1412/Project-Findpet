package vn.FinderPet.FinderPetApplication.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.FinderPet.FinderPetApplication.dao.AuthoritiesDAO;
import vn.FinderPet.FinderPetApplication.dao.UserInfoDAO;
import vn.FinderPet.FinderPetApplication.dao.UsersDAO;
import vn.FinderPet.FinderPetApplication.entity.Authorities;
import vn.FinderPet.FinderPetApplication.entity.UserInfo;
import vn.FinderPet.FinderPetApplication.entity.Users;

import java.util.Optional;

@Service
public class UsersService {
    private UsersDAO usersDAO;
    private AuthoritiesDAO authoritiesDAO;
    private UserInfoDAO userInfoDAO;

    @Autowired
    public UsersService(UsersDAO usersDAO, AuthoritiesDAO authoritiesDAO, UserInfoDAO userInfoDAO) {
        this.usersDAO = usersDAO;
        this.authoritiesDAO = authoritiesDAO;
        this.userInfoDAO = userInfoDAO;
    }

    @Transactional
    public Users createdUser(Users users, UserInfo userInfo){
        Authorities authorities = new Authorities(users, "ROLE_USER");
        authorities.setId(null);
        this.authoritiesDAO.save(authorities);
        this.userInfoDAO.save(userInfo);
        return this.usersDAO.save(users);
    }

    @Transactional
    public Optional<Users> findById(String username){
        return this.usersDAO.findById(username);
    }
}
