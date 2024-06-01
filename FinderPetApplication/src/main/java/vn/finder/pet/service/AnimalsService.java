package vn.finder.pet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.AnimalsDAO;
import vn.finder.pet.entity.Animals;

import java.util.List;

@Service
public class AnimalsService {
    private AnimalsDAO animalsDAO;

    @Autowired
    public AnimalsService(AnimalsDAO animalsDAO) {
        this.animalsDAO = animalsDAO;
    }

    public Animals findById(Long id){
        return this.animalsDAO.findById(id).get();
    }

    public Page<Animals> searchAnimals(List<String> breedType, String breedName, String shelterAddress, List<String> ages, List<Boolean> genders, String size, String name, int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.findByBreedNameContains(breedType, breedName, shelterAddress, ages, genders, size, name, pageable);
    }
}
