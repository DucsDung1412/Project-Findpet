package vn.finder.pet.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.finder.pet.entity.Animals;
import vn.finder.pet.service.AnimalsService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PetController {
    private AnimalsService animalsService;

    @Autowired
    public PetController(AnimalsService animalsService) {
        this.animalsService = animalsService;
    }

    @GetMapping("/pet-grid/{breed_type}")
    public String petGrid(HttpSession session, Model model, @PathVariable String breed_type){
        session.removeAttribute("emailUs");
        Page<Animals> listAnimal = this.animalsService.searchAnimals(Arrays.asList(breed_type), "", "", Arrays.asList("Puppy", "Young", "Adult", "Senior"), Arrays.asList(true, false), "", "", 0, 12);
        model.addAttribute("listAnimal", listAnimal);
        model.addAttribute("breed_type", breed_type);
        return "/pet-grid";
    }

    @GetMapping("/pet-detail")
    public String getPetDetail(@RequestParam String id){
        System.out.println(id);
        return "/pet-detail";
    }

    @RequestMapping("/filter-pet")
    public String processSelection(@RequestBody Map<String ,String> value) {
        String keyword=value.get("filterPet");
        // lựa chọn trong pet-gird để thực hiển tải thông tin cần filter
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
