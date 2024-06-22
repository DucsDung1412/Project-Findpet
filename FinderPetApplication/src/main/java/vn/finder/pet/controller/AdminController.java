package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.finder.pet.service.SheltersService;
import vn.finder.pet.service.UsersService;

@Controller
public class AdminController {
    private SheltersService sheltersService;
    private UsersService usersService;

    @Autowired
    public AdminController(SheltersService sheltersService, UsersService usersService) {
        this.sheltersService = sheltersService;
        this.usersService = usersService;
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model){
        model.addAttribute("totalPending", this.sheltersService.findByShelterStatus("Awaiting").size());
        model.addAttribute("listTotalPending", this.sheltersService.findByShelterStatus("Awaiting"));

        model.addAttribute("totalShelters", this.sheltersService.findAll(0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("listTotalShelters", this.sheltersService.findAll(0, 6).stream());

        model.addAttribute("totalUsers", this.usersService.findAll(0, Integer.MAX_VALUE).getTotalElements());
        model.addAttribute("listTotalUsers", this.usersService.findAll(0, 7).stream());

        return "/admin-dashboard";
    }

    @GetMapping("/admin-shelter-list")
    public String adminShelterList(Model model){
        model.addAttribute("listTotalShelters", this.sheltersService.findAll(0, 10));
        this.sheltersService.findAll(0, 10).stream().forEach(e -> {
            System.out.println(e.getListAnimals().size());
        });
        return "/admin-shelter-list";
    }
}
