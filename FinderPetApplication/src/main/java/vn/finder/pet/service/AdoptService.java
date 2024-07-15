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
import java.util.List;

@Service
public class AdoptService {
    private AdoptDAO adoptDAO;
    private AnimalsService animalsService;
    private UsersService usersService;
    private MailService mailService;

    @Autowired
    public AdoptService(AdoptDAO adoptDAO, AnimalsService animalsService, UsersService usersService, MailService mailService) {
        this.adoptDAO = adoptDAO;
        this.animalsService = animalsService;
        this.usersService = usersService;
        this.mailService = mailService;
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
                adopt.setAdoptDate(Date.valueOf(LocalDate.now()));
                this.adoptDAO.save(adopt);
                mailService.agreePet(this.getUserAdopt(idAnimals),this.getNamePet(idAnimals));
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
                adopt.setAdoptDate(Date.valueOf(LocalDate.now()));
                this.adoptDAO.save(adopt);
                mailService.disablePet(this.getUserAdopt(idAnimals),this.getNamePet(idAnimals));
                return true;
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

    public Integer findByMonthAndShelter(int date, int year, String userName){
        return this.adoptDAO.findByMonthAndShelter(date, year, userName);
    }

    public List<Adopt> findAllNotContains(String status){
        return this.adoptDAO.findAllNotContains(status);
    }

    @Transactional
    public Boolean save(Adopt adopt){
        if(adopt != null){
            if(this.adoptDAO.findByAnimalsAndUsers(adopt.getAnimals(), adopt.getUsers()) == null){
                this.adoptDAO.save(adopt);
            }
            return true;
        }
        return  false;
    }

    public Page<Adopt> findAllAdoptOfShelter(String email, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Users users = this.usersService.findById(email).get();
        return this.adoptDAO.findAllAdoptOfShelter(users.getShelters().getId(), pageable);
    }

    public Page<Adopt> findByUsers(String email, String status, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return this.adoptDAO.findByUsersAndAdoptStatus(email, status, pageable);
    }

    public String getUserAdopt(Long id) {
        String email="";
        Animals animals = this.animalsService.findById(id);
        if(!animals.getListAdopt().isEmpty()){
            Adopt adopt = animals.getListAdopt().get(0);
            email = adopt.getUsers().getUserName();
        }else {
            email = "0";
        }
        return email;
    }

    public String getNamePet(Long id) {
        String animalName = "";
        Animals animals = this.animalsService.findById(id);
        if(!animals.getListAdopt().isEmpty()){
            Adopt adopt = animals.getListAdopt().get(0);
            animalName = adopt.getAnimals().getAnimalName();
        }else {
            animalName = "0";
        }
        return animalName;
    }
}
