package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.AdoptDAO;
import vn.finder.pet.entity.Adopt;
import vn.finder.pet.entity.Animals;
import vn.finder.pet.entity.Users;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class AdoptService {
    private AdoptDAO adoptDAO;
    private AnimalsService animalsService;

    @Autowired
    public AdoptService(AdoptDAO adoptDAO, AnimalsService animalsService) {
        this.adoptDAO = adoptDAO;
        this.animalsService = animalsService;
    }

    @Transactional
    public Boolean disablePet(Long idAnimals){
        try {
            Animals animals = this.animalsService.findById(idAnimals);
            if(animals.getListAdopt().isEmpty()){
                Adopt adopt = new Adopt(null, Date.valueOf(LocalDate.now()), "Adopted", animals.getShelters().getUsers(), animals);
                this.adoptDAO.save(adopt);
                return true;
            } else {
                Adopt adopt = animals.getListAdopt().get(0);
                adopt.setAdopt_status("Adopted");
                this.adoptDAO.save(adopt);
                return true;
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

    @Transactional
    public Boolean enablePet(Long idAnimals) {
        try {
            Animals animals = this.animalsService.findById(idAnimals);
            if(!animals.getListAdopt().isEmpty()){
                Adopt adopt = animals.getListAdopt().get(0);
                adopt.setAdopt_status("Cancel");
                this.adoptDAO.save(adopt);
                return true;
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

    public Integer findByMonthAndShelter(int date, String userName){
        return this.adoptDAO.findByMonthAndShelter(date, userName);
    }
}
