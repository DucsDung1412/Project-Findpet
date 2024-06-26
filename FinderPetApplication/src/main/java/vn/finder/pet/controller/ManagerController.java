package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.finder.pet.entity.Animals;
import vn.finder.pet.entity.DtoPetShelters;
import vn.finder.pet.service.*;

import java.util.ArrayList;
import java.util.Calendar;

@Controller
public class ManagerController {
    private AnimalsService animalsService;
    private AdoptService adoptService;
    private FavoritesService favoritesService;
    private UsersService usersService;
    private SheltersService sheltersService;

    @Autowired
    public ManagerController(AnimalsService animalsService, AdoptService adoptService, FavoritesService favoritesService, UsersService usersService, SheltersService sheltersService) {
        this.animalsService = animalsService;
        this.adoptService = adoptService;
        this.favoritesService = favoritesService;
        this.usersService = usersService;
        this.sheltersService = sheltersService;
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
        model.addAttribute("user", this.usersService.findById(this.getEmailLogin()).get());

        ArrayList<Integer> listInteraction = new ArrayList<>();
        listInteraction.add(this.favoritesService.findByMonthAndShelter(1, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(1, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(2, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(2, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(3, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(3, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(4, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(4, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(5, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(5, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(6, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(6, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(7, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(7, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(8, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(8, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(9, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(9, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(10, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(10, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(11, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(11, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));
        listInteraction.add(this.favoritesService.findByMonthAndShelter(12, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()) + this.adoptService.findByMonthAndShelter(12, Calendar.getInstance().get(Calendar.YEAR), this.getEmailLogin()));

        model.addAttribute("listInteraction", listInteraction);

        System.out.println(Calendar.getInstance().get(Calendar.YEAR));
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
        model.addAttribute("user", this.usersService.findById(this.getEmailLogin()).get());
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
        model.addAttribute("user", this.usersService.findById(this.getEmailLogin()).get());
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

    @GetMapping("/add-listing-minimal")
    public String addListingMinimal(Model model){
        model.addAttribute("user", this.usersService.findById(this.getEmailLogin()).get());
        return "/add-listing-minimal";
    }

    @GetMapping("/agent-notify")
    public String agentNotify(Model model, @RequestParam(value = "page", required = false) String page){
        if(page == null){
            page = "0";
        }
        int pg = Integer.valueOf(page);
        model.addAttribute("user", this.usersService.findById(this.getEmailLogin()).get());
        model.addAttribute("listNotify", this.adoptService.findAllAdoptOfShelter(this.getEmailLogin(), pg == 0 ? 0 : pg - 1, 10));
        model.addAttribute("page", pg == 0 ? 1 : pg);
        return "/agent-notify";
    }

    @GetMapping("/changePage-agentNotify")
    public String changePageAgentNotify(@RequestParam("page") int page, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("page", page + 1);
        return "redirect:/agent-notify";
    }

    @PostMapping("/uploadpet")
    public String auto1(@RequestBody DtoPetShelters bd, RedirectAttributes atr) {

        int flag = 0;
        if (bd.getAnimalName().isEmpty()) {
            atr.addAttribute("message", "Chưa nhập tên thằng lồn");
            flag++;
        }
        if (bd.getAnimal_info_characteristics().isEmpty()) {
            flag++;
        }
        if (bd.getAnimalAvatar().isEmpty()) {
            flag++;
        }
        if (bd.getBreed_name().isEmpty()) {
            flag++;
        }
        if (bd.getAnimal_info_description().isEmpty()) {
            flag++;
        }
        if (flag == 0) {
            if(animalsService.checkAnimal(bd.getBreed_name(),
                    bd.getBreed_type(),
                    bd.getAnimalName(),
                    bd.isAnimalGender(),
                    bd.getAnimalSize(),
                    bd.getAnimalAge())){
                sheltersService.createShelter(bd, this.getEmailLogin());
            };

        }
        return "redirect:/add-listing-minimal";
    }
}
