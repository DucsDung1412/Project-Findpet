package vn.FinderPet.FinderPetApplication.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vn.FinderPet.FinderPetApplication.entity.AnimalInfoRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MyRestController {
    @PostMapping("/search-pet")
    public Map<String, Object> searchPet(@RequestBody AnimalInfoRequest animalInfoRequest){
        System.out.println(animalInfoRequest.toString());
        Map<String, Object> map = new HashMap<>();
        return map;
    }
}
