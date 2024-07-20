package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.BreedDAO;
import vn.finder.pet.entity.Breed;

import java.util.List;

@Service
public class BreedService {
    private BreedDAO breedDAO;

    @Autowired
    public BreedService(BreedDAO breedDAO) {
        this.breedDAO = breedDAO;
    }

    @Transactional
    public Boolean save(Breed breed){
        try {
            this.breedDAO.save(breed);
            return true;
        } catch (Exception e){

        }
        return false;
    }

    public List<Breed> findByBreed_type(String breedType){
        return this.breedDAO.findByBreed_type(breedType);
    }
}
