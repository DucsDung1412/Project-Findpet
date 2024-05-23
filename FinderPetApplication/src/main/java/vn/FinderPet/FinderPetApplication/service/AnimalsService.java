package vn.FinderPet.FinderPetApplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.FinderPet.FinderPetApplication.dao.AnimalsDAO;
import vn.FinderPet.FinderPetApplication.entity.Animals;

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

    public Page<Animals> searchAnimals(String breedName, String shelterAddress, List<String> ages, List<Boolean> genders, String size, String name, int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.findByBreedNameContains(breedName, shelterAddress, ages, genders, size, name, pageable);
    }
}
