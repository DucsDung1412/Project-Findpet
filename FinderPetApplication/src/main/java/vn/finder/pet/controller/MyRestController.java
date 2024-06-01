package vn.finder.pet.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.finder.pet.entity.AnimalInfoRequest;
import vn.finder.pet.service.AnimalsService;

import java.util.*;

@RestController
public class MyRestController {
    private AnimalsService animalsService;

    @Autowired
    public MyRestController(AnimalsService animalsService) {
        this.animalsService = animalsService;
    }

    @PostMapping("/remove-session-user")
    public Boolean removeSessionUser(HttpSession session){
        session.removeAttribute("userLogin");
        return true;
    }

    @PostMapping("/search-pet")
    public Map<String, Object> searchPet(@RequestBody Object hashMap){
        AnimalInfoRequest animalInfoRequest = new AnimalInfoRequest();
        animalInfoRequest.setLocation(((Map<String, String>)hashMap).get("location").isEmpty() ? "%" : ((HashMap<String, String>)hashMap).get("location"));
        animalInfoRequest.setBreed(((HashMap<String, String>)hashMap).get("breed").isEmpty() ? "%" : ((HashMap<String, String>)hashMap).get("breed"));
        animalInfoRequest.setAge(((List<String>) ((HashMap<?, ?>) hashMap).get("age")).isEmpty() ? Arrays.asList("Puppy", "Young", "Adult", "Senior") : (List<String>) ((HashMap<?, ?>) hashMap).get("age"));
        animalInfoRequest.setPageNumber((int) ((HashMap<?, ?>)hashMap).get("page"));
        animalInfoRequest.setSizePage((int) ((HashMap<?, ?>)hashMap).get("sizePage"));
        animalInfoRequest.setGender( ((List<Boolean>) ((HashMap<?, ?>) hashMap).get("gender")).isEmpty() ?  Arrays.asList(true, false) : ((List<Boolean>) ((HashMap<?, ?>) hashMap).get("gender")));
        animalInfoRequest.setSize(((Map<String, String>)hashMap).get("size").isEmpty() ? "%" : ((String)((Map<String, String>)hashMap).get("size")).trim().substring(0, ((Map<String, String>)hashMap).get("size").trim().indexOf(" (")));
        animalInfoRequest.setName(((Map<String, String>)hashMap).get("name").isEmpty() ? "%" : ((String)((Map<String, String>)hashMap).get("name")).trim());
        animalInfoRequest.setBreedType(((List<String>) ((HashMap<?, ?>) hashMap).get("breedType")).isEmpty() ? Arrays.asList("") : (List<String>) ((HashMap<?, ?>) hashMap).get("breedType"));

        System.out.println(this.animalsService.searchAnimals(animalInfoRequest.getBreedType(), animalInfoRequest.getBreed(), animalInfoRequest.getLocation(), animalInfoRequest.getAge(), animalInfoRequest.getGender(), animalInfoRequest.getSize(), animalInfoRequest.getName(), animalInfoRequest.getPageNumber(), animalInfoRequest.getSizePage()).stream().toList());

        Map<String, Object> map = new HashMap<>();
        return map;
    }
}














