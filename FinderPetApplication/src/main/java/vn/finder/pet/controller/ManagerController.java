package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.finder.pet.entity.Animals;
import vn.finder.pet.service.AdoptService;
import vn.finder.pet.service.AnimalsService;
import vn.finder.pet.service.FavoritesService;
import vn.finder.pet.service.UsersService;

import java.util.ArrayList;

@Controller
public class ManagerController {
    private AnimalsService animalsService;
    private AdoptService adoptService;
    private FavoritesService favoritesService;
    private UsersService usersService;

    @Autowired
    public ManagerController(AnimalsService animalsService, AdoptService adoptService, FavoritesService favoritesService, UsersService usersService) {
        this.animalsService = animalsService;
        this.adoptService = adoptService;
        this.favoritesService = favoritesService;
        this.usersService = usersService;
    }

    public String getEmailLogin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";
        if (authentication != null) {
            if(this.usersService.findById(authentication.getName()).isEmpty()){
                OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                email = principal.getAttribute("email");
            } else {
                email = authentication.getName();
            }
        }
        return email;
    }

    @GetMapping("/agent-dashboard")
    public String agentDashboard(Model model){
        model.addAttribute("availablePets", this.animalsService.findCountAnimalsAvailable(this.getEmailLogin()));
        model.addAttribute("awaitingApproval", this.animalsService.findCountAwaitingAnimals(this.getEmailLogin()));
        model.addAttribute("totalInteractions", this.animalsService.findCountAdopt(this.getEmailLogin()) + this.animalsService.findCountFavorite(this.getEmailLogin()));
        model.addAttribute("listAwaiting", this.animalsService.findByStatus(this.getEmailLogin(), "Awaiting", 0, 6));
        return "/agent-dashboard";
    }

    @GetMapping("/agent-listings")
    public String agentListings(Model model){
        model.addAttribute("allPets", this.animalsService.findCountAnimalsInShelter(this.getEmailLogin()));
        model.addAttribute("awaitingPets", this.animalsService.findCountAwaitingAnimals(this.getEmailLogin()));
        model.addAttribute("adpotedPets", this.animalsService.findCountAnimalsAdopted(this.getEmailLogin()));
        model.addAttribute("availablePets", this.animalsService.findCountAnimalsAvailable(this.getEmailLogin()));
        Page<Animals> listAnimals = this.animalsService.findAllPet(this.getEmailLogin(), 0, 6);
        ArrayList<Long> l = new ArrayList<>();
        listAnimals.stream().toList().forEach(e -> {
            for (int i = 0; i < e.getListAdopt().size(); i++) {
                if(e.getListAdopt().get(i).getAnimals().getId() == e.getId()){
                    l.add(e.getListAdopt().get(i).getAnimals().getId());
                    break;
                }
            }
        });
        model.addAttribute("listAdopt", l);
        model.addAttribute("listAnimals", listAnimals);
        return "/agent-listings";
    }
}
