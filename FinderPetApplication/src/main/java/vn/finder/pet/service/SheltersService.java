package vn.finder.pet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.ShelterDAO;
import vn.finder.pet.entity.Shelters;

import java.util.List;

@Service
public class SheltersService {
    private ShelterDAO shelterDAO;

    @Autowired
    public SheltersService(ShelterDAO shelterDAO) {
        this.shelterDAO = shelterDAO;
    }

    public List<Shelters> findByShelterStatus(String awaiting) {
        return this.shelterDAO.findByShelterStatus(awaiting);
    }

    public Page<Shelters> findAll(int page, int sizePage){
        Pageable pageable = PageRequest.of(page, sizePage);
        return this.shelterDAO.findSheltersByStatusNotContaining(pageable, "Awaiting");
    }
}
