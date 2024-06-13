package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    public String agentDashboard(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);

        model.addAttribute("availablePets", this.animalsService.findCountAnimalsAvailable(this.getEmailLogin()));
        model.addAttribute("awaitingApproval", this.animalsService.findCountAwaitingAnimals(this.getEmailLogin()));
        model.addAttribute("totalInteractions", this.animalsService.findCountAdopt(this.getEmailLogin()) + this.animalsService.findCountFavorite(this.getEmailLogin()));
        model.addAttribute("listAwaiting", this.animalsService.findByStatus(this.getEmailLogin(), "Awaiting",  pg == 0 ? 0 : pg - 1, 6));
        model.addAttribute("page", pg == 0 ? 1 : pg);
        return "/agent-dashboard";
    }

    @GetMapping("/changePage-agentDashboard")
    public String changePageAgentDashboard(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/agent-dashboard";
    }

    @GetMapping("/disable-pet-dashboard")
    public String disablePetDashboard(@RequestParam(value = "id") Long id){
        this.adoptService.disablePet(id);
        return "redirect:/agent-dashboard";
    }

    @GetMapping("/enable-pet-dashboard")
    public String enablePetDashboard(@RequestParam(value = "id") Long id){
        this.adoptService.enablePet(id);
        return "redirect:/agent-dashboard";
    }

    @GetMapping("/agent-listings")
    public String agentListings(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);

        model.addAttribute("allPets", this.animalsService.findCountAnimalsInShelter(this.getEmailLogin()));
        model.addAttribute("awaitingPets", this.animalsService.findCountAwaitingAnimals(this.getEmailLogin()));
        model.addAttribute("adpotedPets", this.animalsService.findCountAnimalsAdopted(this.getEmailLogin()));
        model.addAttribute("availablePets", this.animalsService.findCountAnimalsAvailable(this.getEmailLogin()));
        Page<Animals> listAnimals = this.animalsService.findAllPet(this.getEmailLogin(), pg == 0 ? 0 : pg - 1, 6);
        ArrayList<Long> l = new ArrayList<>();
        listAnimals.stream().toList().forEach(e -> {
            for (int i = 0; i < e.getListAdopt().size(); i++) {
                if(e.getListAdopt().get(i).getAnimals().getId() == e.getId() && !e.getListAdopt().get(i).getAdopt_status().equals("Cancel")){
                    l.add(e.getListAdopt().get(i).getAnimals().getId());
                    break;
                }
            }
        });
        model.addAttribute("listAdopt", l);
        model.addAttribute("listAnimals", listAnimals);
        model.addAttribute("page", pg == 0 ? 1 : pg);
        return "/agent-listings";
    }

    @GetMapping("/changePage-agentListings")
    public String changePageAgentListings(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/agent-listings";
    }

    @GetMapping("/disable-pet")
    public String disablePet(@RequestParam(value = "id") Long id){
        this.adoptService.disablePet(id);
        return "redirect:/agent-listings";
    }

    @GetMapping("/enable-pet")
    public String enablePet(@RequestParam(value = "id") Long id){
        this.adoptService.enablePet(id);
        return "redirect:/agent-listings";
    }

    @GetMapping("/agent-bookings")
    public String agentBookings(Model model, @RequestParam(value = "page", required = false) String page, @RequestParam(value = "status", required = false) String status){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        if(status == null){
            status = "%";
        }
        model.addAttribute("listAdopt", this.animalsService.findByStatus(this.getEmailLogin(), status, pg == 0 ? 0 : pg - 1, 10));
        model.addAttribute("page", pg == 0 ? 1 : pg);
        return "/agent-bookings";
    }

    @GetMapping("/changePage-agentBooking")
    public String changePageAgentBooking(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/agent-bookings";
    }

    @GetMapping("/disable-pet-bookings")
    public String disablePetBookings(@RequestParam(value = "id") Long id){
        this.adoptService.disablePet(id);
        return "redirect:/agent-bookings";
    }

    @GetMapping("/enable-pet-bookings")
    public String enablePetBookings(@RequestParam(value = "id") Long id){
        this.adoptService.enablePet(id);
        return "redirect:/agent-bookings";
    }
}
