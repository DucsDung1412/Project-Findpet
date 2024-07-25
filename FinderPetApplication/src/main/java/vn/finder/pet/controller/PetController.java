package vn.finder.pet.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.entity.Animals;
import vn.finder.pet.entity.Users;
import vn.finder.pet.service.AdoptService;
import vn.finder.pet.service.AnimalsService;
import vn.finder.pet.service.BreedService;
import vn.finder.pet.service.UsersService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pet-list")
public class PetController {
    private AnimalsService animalsService;
    private UsersService usersService;
    private AdoptService adoptService;
    private BreedService breedService;

    @Autowired
    public PetController(AnimalsService animalsService, UsersService usersService, AdoptService adoptService, BreedService breedService) {
        this.animalsService = animalsService;
        this.usersService = usersService;
        this.adoptService = adoptService;
        this.breedService = breedService;
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

    @GetMapping("/pet-search")
    public String petGetTemp(RedirectAttributes redirectAttributes, @RequestParam(value = "location") String location, @RequestParam(value = "breed_type") String breed_type){
        redirectAttributes.addAttribute("breed_type", breed_type);
        redirectAttributes.addAttribute("location", location);
        return "redirect:/pet-list/" + breed_type;
    }

    @GetMapping("/{breed_type}")
    public String petGrid(HttpSession session, Model model, @PathVariable String breed_type
                            , @RequestParam(value = "breed", required = false) String breed
                            , @RequestParam(value = "age", required = false) String age
                            , @RequestParam(value = "size", required = false) String size
                            , @RequestParam(value = "gender", required = false) Boolean gender
                            , @RequestParam(value = "location", required = false) String location) {
        session.removeAttribute("emailUs");

        List<Long> listAdoptId = new ArrayList<>();
        this.adoptService.findAllNotContains("Cancel").forEach(e -> {
            listAdoptId.add(e.getAnimals().getId());
        });

        if(breed_type.equals("All")){
            breed_type = "%";
        }

        Page<Animals> listAnimal = this.animalsService.searchAnimals(Arrays.asList(breed_type), breed == null ? "" : breed, location == null ? "" : location, breed_type.equals("Cat") ? (age == null ? Arrays.asList("Kitten", "Young", "Adult", "Senior") : Arrays.asList(age)) : (age == null ? Arrays.asList("Puppy", "Young", "Adult", "Senior") : Arrays.asList(age)), gender == null ? Arrays.asList(true, false) : Arrays.asList(gender), size == null ? "" : size, "", listAdoptId, 0, 12);

        if(breed_type.equals("Cat")){
            model.addAttribute("age_type", "Kitten");
        } else {
            model.addAttribute("age_type", "Puppy");
        }

        ArrayList<Long> listFavorites = new ArrayList<>();
        if(this.getEmailLogin() != null){
            Users users = this.usersService.findById(this.getEmailLogin()).get();
            users.getListFavorites().forEach(e -> {
                listFavorites.add(e.getAnimals().getId());
            });
        }

        model.addAttribute("listFavorites", listFavorites);
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("listAnimal", listAnimal);
        model.addAttribute("breed_type", breed_type);
        model.addAttribute("listBreedType", this.breedService.findByBreed_type(breed_type));
        return "/pet-grid";
    }

    @GetMapping("/pet-detail")
    public String getPetDetail(@RequestParam Long id, Model model, @RequestParam(value = "status", required = false) String status){
        model.addAttribute("user", this.getEmailLogin() == null ? null : this.usersService.findById(this.getEmailLogin()).get());
        Animals animals = this.animalsService.findById(id);
        model.addAttribute("listSameAnimal", this.animalsService.findByBreedOrderByCustom(animals.getBreed().getBreed_name(), animals.getBreed().getBreed_type(), 0, 4).stream().toList());
        model.addAttribute("animal", animals);
        model.addAttribute("id",id);
        if(status != null){
            model.addAttribute("status", status);
        }
        return "/pet-detail";
    }

    @RequestMapping("/filter-pet")
    public String processSelection(@RequestBody Map<String ,String> value) {
        String keyword=value.get("filterPet");
//         lựa chọn trong pet-gird để thực hiển tải thông tin cần filter
        switch (keyword) {
            case "All":
                // hiển thị tất cả các con pet
                System.out.print(animalsService.findAll(0,2).stream().toList());
                break;
            case "Name-Pet(A-Z)":
                //Lọc tất cả tên pet từ A-Z
                System.out.print(animalsService.filterName(0, 2).stream().toList());
                break;
            case "Date-Submitted":
                //Lọc tất cả pet theo ngày đăng gần nhất
                System.out.print(animalsService.filterDate(0, 2).stream().toList());
                break;
            case "Top-rated":
                //Lọc tất cả pet theo lượt thích cao nhất tới thấp nhất
                System.out.print(animalsService.filterFavorite(0, 4).stream().toList());
                break;
        }
        return "redirect:/pet-grid";
    }
}
