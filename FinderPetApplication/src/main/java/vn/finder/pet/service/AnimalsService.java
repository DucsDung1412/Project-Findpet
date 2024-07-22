package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.AnimalsDAO;
import vn.finder.pet.entity.Adopt;
import vn.finder.pet.entity.Animals;
import vn.finder.pet.entity.DtoPetShelters;
import vn.finder.pet.entity.Users;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalsService {
    private AnimalsDAO animalsDAO;
    private UsersService usersService;

    @Autowired
    public AnimalsService(AnimalsDAO animalsDAO, UsersService usersService) {
        this.animalsDAO = animalsDAO;
        this.usersService = usersService;
    }

    public Animals findById(Long id){
        return this.animalsDAO.findById(id).get();
    }

    public Page<Animals> searchAnimals(List<String> breedType, String breedName, String shelterAddress, List<String> ages, List<Boolean> genders, String size, String name, List<Long> adopt, int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.findByBreedNameContains(breedType, breedName, shelterAddress, ages, genders, size, name, adopt, pageable);
    }

    public Page<Animals> findAll(int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.findAll(pageable);
    }

    public Page<Animals> filterName(int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.filterName(pageable);
    }

    public Page<Animals> filterDate(int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.filterDate(pageable);
    }

    public Page<Animals> filterFavorite(int page, int sizePage) {
        Pageable pageable = PageRequest.of(page, sizePage);
        return animalsDAO.filterFavorite(pageable);
    }

    public Integer findCountAnimalsAvailable(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountAnimalsAvailable(users.getShelters().getId());
    }

    public Integer findCountAnimalsInShelter(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountAnimalsInShelter(users.getShelters().getId());
    }

    public Integer findCountAnimalsAdopted(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountAnimalsAdopted(users.getShelters().getId());
    }

    public Integer findCountAwaitingAnimals(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountAwaitingAnimals(users.getShelters().getId());
    }

    public Integer findCountAdopt(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountAdopt(users.getShelters().getId());
    }

    public Page<Adopt> findByStatus(String email, String status, int page, int sizePage){
        Pageable pageable = PageRequest.of(page, sizePage);
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findByStatus(status, pageable, users.getShelters().getId());
    }

    public Integer findCountFavorite(String email){
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findCountFavorite(users.getShelters().getId());
    }

    public Page<Animals> findAllPet(String email, int page, int sizePage){
        Pageable pageable = PageRequest.of(page, sizePage);
        Users users = this.usersService.findById(email).get();
        return this.animalsDAO.findAllPet(pageable, users.getShelters().getId());
    }

    public Page<Animals> findRandom(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return this.animalsDAO.findRandom(pageable);
    }

    public Page<Animals> findByShelterAddressOrderByCustom(int page, int size, String shelterAddress){
        Pageable pageable = PageRequest.of(page, size);
        return this.animalsDAO.findByShelterAddressOrderByCustom(shelterAddress, pageable);
    }

    public Page<Animals> findByBreedOrderByCustom(String breedName, String breedType, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return this.animalsDAO.findByBreedOrderByCustom(breedName, breedType, pageable);
    }

    public Boolean checkAnimal(String breedName, String breedType,String animalName,boolean animalGender,String animalSize,String animalAge){
        List<Animals> animals = animalsDAO.findAnimalsByCriteria(breedName,breedType,animalName,animalGender,animalSize,animalAge);
        return animals.isEmpty();
    }

    @Transactional
    public Boolean save(Animals animal){
        try {
            this.animalsDAO.save(animal);
            return true;
        } catch (Exception e){
            System.out.println(e);
        }
        return false;
    }

    @Transactional
    public Boolean updateAnimal(DtoPetShelters bd, Long id){
        Optional<Animals> animals = this.animalsDAO.findById(id);
        if(!animals.isEmpty()){
//            animals.get().getBreed().setBreed_type(bd.getBreed_type());
//            animals.get().getBreed().setBreed_name(bd.getBreed_name());
            animals.get().setAnimalName(bd.getAnimalName());
            animals.get().setAnimalSize(bd.getAnimalSize());
            animals.get().setAnimalAge(bd.getAnimalAge());
            animals.get().setAnimalGender(bd.isAnimalGender());
            animals.get().setAnimalAvatar(bd.getAnimalAvatar());
            animals.get().getAnimalInfo().setAnimalInfoColor(bd.getAnimal_info_color());
            animals.get().getAnimalInfo().setAnimalInfoLeg(bd.getAnimal_info_leg());
            animals.get().getAnimalInfo().setAnimalInfoHarmony(bd.getAnimal_info_harmony());
            animals.get().getAnimalInfo().setAnimalInfoHealth(bd.getAnimal_info_health());
            animals.get().getAnimalInfo().setAnimalInfoCharacteristics(bd.getAnimal_info_characteristics());
            animals.get().getAnimalInfo().setAnimalInfoDescription(bd.getAnimal_info_description());
            System.out.println("--------------");
            System.out.println("--------------");
            System.out.println("--------------");
            System.out.println("--------------");
            System.out.println(animals.get().getListAvatar());
            System.out.println("--------------");
            System.out.println("--------------");
            System.out.println("--------------");
            this.animalsDAO.save(animals.get());
        }
        return false;
    }
}
