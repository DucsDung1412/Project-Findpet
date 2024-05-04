package vn.FinderPet.FinderPetApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.FinderPet.FinderPetApplication.entity.UserInfo;

@RepositoryRestResource(path = "user-info")
public interface UserInfoDAO extends JpaRepository<UserInfo, Long> {

}