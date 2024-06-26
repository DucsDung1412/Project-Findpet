package vn.finder.pet.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.finder.pet.entity.Adopt;

@RepositoryRestResource(path = "adopt")
public interface AdoptDAO extends JpaRepository<Adopt, Long> {
    @Query("SELECT COUNT(ad.id) "
            + "FROM Animals a "
            + "INNER JOIN a.listAdopt ad "
            + "INNER JOIN a.shelters s "
            + "WHERE MONTH(ad.adoptDate) = :date AND s.users.userName LIKE %:userName%")
    Integer findByMonthAndShelter(@Param("date") int date, @Param("userName") String userName);
}
