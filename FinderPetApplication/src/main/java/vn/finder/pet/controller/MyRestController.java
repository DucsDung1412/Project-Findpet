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
    public ArrayList<Map<String, Object>> searchPet(@RequestBody Object hashMap){
        ArrayList<Map<String, Object>> listSearch = new ArrayList<>();

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


        System.out.println(animalInfoRequest.getBreedType());
        System.out.println(animalInfoRequest.getBreed());
        System.out.println(animalInfoRequest.getLocation());
        System.out.println(animalInfoRequest.getAge());
        System.out.println(animalInfoRequest.getGender());
        System.out.println(animalInfoRequest.getSize());
        System.out.println(animalInfoRequest.getName());
        System.out.println(animalInfoRequest.getPageNumber());
        System.out.println(animalInfoRequest.getSizePage());

        this.animalsService.searchAnimals(animalInfoRequest.getBreedType(), animalInfoRequest.getBreed(), animalInfoRequest.getLocation(), animalInfoRequest.getAge(), animalInfoRequest.getGender(), animalInfoRequest.getSize(), animalInfoRequest.getName(), animalInfoRequest.getPageNumber(), animalInfoRequest.getSizePage()).stream().toList().forEach(e -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", e.getId());
            map.put("avatar", e.getAnimalAvatar());
            map.put("breed", e.getBreed().getBreed_name());
            map.put("name", e.getAnimalName());
            map.put("age", e.getAnimalAge());
            map.put("size", e.getAnimalSize());
            map.put("gender", e.getAnimalGender());
            System.out.println(e.getId());
            listSearch.add(map);
        });

        return listSearch;
    }
}














