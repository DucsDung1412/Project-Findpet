package vn.finder.pet.controller;

import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.finder.pet.entity.AnimalInfoRequest;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.AnimalsService;
import vn.finder.pet.service.UsersService;

import java.util.*;

@RestController
public class MyRestController {
    private AnimalsService animalsService;
    private UsersService usersSerice;

    @Autowired
    public MyRestController(AnimalsService animalsService, UsersService usersSerice) {
        this.animalsService = animalsService;
        this.usersSerice = usersSerice;
    }

    @PostMapping("/search-pet")
    public Map<String, Object> searchPet(@RequestBody Object hashMap) {
        AnimalInfoRequest animalInfoRequest = new AnimalInfoRequest();
        animalInfoRequest.setLocation(((Map<String, String>) hashMap).get("location").isEmpty() ? "%" : ((HashMap<String, String>) hashMap).get("location"));
        animalInfoRequest.setBreed(((HashMap<String, String>) hashMap).get("breed").isEmpty() ? "%" : ((HashMap<String, String>) hashMap).get("breed"));
        animalInfoRequest.setAge(((List<String>) ((HashMap<?, ?>) hashMap).get("age")).isEmpty() ? Arrays.asList("Puppy", "Young", "Adult", "Senior") : (List<String>) ((HashMap<?, ?>) hashMap).get("age"));
        animalInfoRequest.setPageNumber((int) ((HashMap<?, ?>) hashMap).get("page"));
        animalInfoRequest.setSizePage((int) ((HashMap<?, ?>) hashMap).get("sizePage"));
        animalInfoRequest.setGender(((List<Boolean>) ((HashMap<?, ?>) hashMap).get("gender")).isEmpty() ? Arrays.asList(true, false) : ((List<Boolean>) ((HashMap<?, ?>) hashMap).get("gender")));
        animalInfoRequest.setSize(((Map<String, String>) hashMap).get("size").isEmpty() ? "%" : ((String) ((Map<String, String>) hashMap).get("size")).trim().substring(0, ((Map<String, String>) hashMap).get("size").trim().indexOf(" (")));
        animalInfoRequest.setName(((Map<String, String>) hashMap).get("name").isEmpty() ? "%" : ((String) ((Map<String, String>) hashMap).get("name")).trim());

        System.out.println(this.animalsService.searchAnimals(animalInfoRequest.getBreed(), animalInfoRequest.getLocation(), animalInfoRequest.getAge(), animalInfoRequest.getGender(), animalInfoRequest.getSize(), animalInfoRequest.getName(), animalInfoRequest.getPageNumber(), animalInfoRequest.getSizePage()).stream().toList());

        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @GetMapping("/request-profile")
    public Users requestProfile(HttpSession session) {
        String email = session.getAttribute("email").toString();
        System.out.println(email);
        if(email.isEmpty()){
            return null;
        }else{
            Optional<Users> u = usersSerice.findById(email);
            System.out.println(u.toString());
            return u.orElse(null);
        }
    }
}














