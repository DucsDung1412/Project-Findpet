package vn.finder.pet.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import vn.finder.pet.entity.AnimalInfoRequest;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.AdoptService;
import vn.finder.pet.service.AnimalsService;
import vn.finder.pet.service.FavoritesService;
import vn.finder.pet.service.UsersService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MyRestController {
    private AnimalsService animalsService;
    private UsersService usersService;
    private FavoritesService favoritesService;
    private AdoptService adoptService;

    @Autowired
    public MyRestController(AnimalsService animalsService, UsersService usersService, FavoritesService favoritesService, AdoptService adoptService) {
        this.animalsService = animalsService;
        this.usersService = usersService;
        this.favoritesService = favoritesService;
        this.adoptService = adoptService;
    }

    public String getEmailLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";
        if (authentication != null && !authentication.getName().equals("anonymousUser")) {
            if(this.usersService.findById(authentication.getName()).isEmpty()){
                OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                email = principal.getAttribute("email");
            } else {
                email = authentication.getName();
            }
        } else {
            return null;
        }
        return email;
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

        ArrayList<Long> listFavorites = new ArrayList<>();
        if(this.getEmailLogin() != null){
            Users users = this.usersService.findById(this.getEmailLogin()).get();
            users.getListFavorites().forEach(e -> {
                listFavorites.add(e.getAnimals().getId());
            });
        }

        List<Long> listAdoptId = new ArrayList<>();
        this.adoptService.findAllNotContains("Cancel").forEach(e -> {
            listAdoptId.add(e.getAnimals().getId());
        });

        this.animalsService.searchAnimals(animalInfoRequest.getBreedType(), animalInfoRequest.getBreed(), animalInfoRequest.getLocation(), animalInfoRequest.getAge(), animalInfoRequest.getGender(), animalInfoRequest.getSize(), animalInfoRequest.getName(), listAdoptId, animalInfoRequest.getPageNumber(), animalInfoRequest.getSizePage()).stream().toList().forEach(e -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", e.getId());
            map.put("avatar", e.getAnimalAvatar());
            map.put("breed", e.getBreed().getBreed_name());
            map.put("name", e.getAnimalName());
            map.put("age", e.getAnimalAge());
            map.put("size", e.getAnimalSize());
            map.put("gender", e.getAnimalGender());
            listSearch.add(map);
        });

        Map<String, Object> mapFavorite = new HashMap<>();
        mapFavorite.put("listFavorite", listFavorites);
        listSearch.add(mapFavorite);

        return listSearch;
    }

    @GetMapping("/removeFavorite")
    public String removeFavorite(@RequestParam(value = "id") Long id){
        Users users = this.usersService.findById(this.getEmailLogin()).get();
        if(users != null){
            AtomicInteger i = new AtomicInteger();
            users.getListFavorites().forEach(e -> {
               if(e.getAnimals().getId() == id){
                   i.getAndIncrement();
                   System.out.println(e.getId());
                   this.favoritesService.removeOne(e.getId());
               }
            });
            if(i.get() > 0){
                return "removeFavorite";
            }
        }
        return "false";
    }

    @GetMapping("/addFavorite")
    public String addFavorite(@RequestParam(value = "id") Long id){
        Users users = this.usersService.findById(this.getEmailLogin()).get();
        if(users != null){
            AtomicInteger i = new AtomicInteger();
            users.getListFavorites().forEach(e -> {
                if(e.getAnimals().getId() == id){
                    i.getAndIncrement();
                }
            });
            if(i.get() == 0){
                this.favoritesService.save(id, users);
                return "addFavorite";
            }
        }
        return "false";
    }
}














