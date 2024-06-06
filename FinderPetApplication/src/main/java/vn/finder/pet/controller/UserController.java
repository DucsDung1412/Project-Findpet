package vn.finder.pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import vn.finder.pet.common.PasswordCommon;
import vn.finder.pet.entity.Users;
import vn.finder.pet.security.MySecurities;
import vn.finder.pet.service.UsersService;

@Controller
public class UserController {
    UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/accountprofile/changepassword")
    public String changePassword(@RequestParam("currentpassword") String currentpassword,
                                 @RequestParam("newpassword") String newpassword,
                                 @RequestParam("confirmpassword") String confirmpassword) {

        if (currentpassword.isEmpty() || newpassword.isEmpty() || confirmpassword.isEmpty()) {
            System.out.println("Lỗi trống field");
        }
        if (PasswordCommon.validatePatternPassword(newpassword)) {
            System.out.println("Lỗi định dạng password");
        }
        if (!newpassword.equals(confirmpassword)) {
            System.out.println("2 password nhập không giống nhau");
        }
        if (currentpassword.equals(newpassword)) {
            System.out.println("Pass cũ không được trùng với pass mới");
        }
        if(usersService.changePasswordProfileService(currentpassword,newpassword)){
            System.out.println("Đổi thành công");
        }else{
            System.out.println("pass new không trùng với pass cũ");
        }
        return "redirect:/account-profile";
    }

    @PostMapping("/accountprofile/changeprofile")
    public String changeProfile(@ModelAttribute Users users, @RequestParam("profilePhoto") MultipartFile file) {
        try {
            System.out.println("User nhận từ web: " + users.toString());
            usersService.changeProfileService(file, users.getFirstName(), users.getLastName(), users.getCountry(), users.getAddress(), users.getPhone());
        } catch (Exception e) {
            System.out.println("Lỗi update profile");
        }
        return "redirect:/account-profile";
    }
}
