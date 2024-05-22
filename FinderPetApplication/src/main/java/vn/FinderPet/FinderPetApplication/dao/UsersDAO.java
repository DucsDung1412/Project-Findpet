package vn.FinderPet.FinderPetApplication.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import vn.FinderPet.FinderPetApplication.entity.Users;

import java.util.Collection;

@RepositoryRestResource(path = "users")
public interface UsersDAO extends JpaRepository<Users, String> {

}
