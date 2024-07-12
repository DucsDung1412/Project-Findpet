package vn.finder.pet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Spons;

@RepositoryRestResource(path = "spons")
public interface SponsDAO extends JpaRepository<Spons, Long> {
    public Page<Spons> findAll(Pageable pageable);

    @Query("SELECT s FROM Spons s WHERE s.users.userName LIKE :userName")
    public Page<Spons> findAllByUser(Pageable pageable, @Param("userName") String userName);
}
