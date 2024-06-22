package vn.finder.pet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Users;

@RepositoryRestResource(path = "users")
public interface UsersDAO extends JpaRepository<Users, String> {
    public Page<Users> findAll(Pageable pageable);
}
