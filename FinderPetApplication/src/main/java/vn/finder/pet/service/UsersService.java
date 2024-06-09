package vn.finder.pet.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.finder.pet.dao.AuthoritiesDAO;
import vn.finder.pet.dao.UsersDAO;
import vn.finder.pet.entity.Authorities;
import vn.finder.pet.entity.Favorites;
import vn.finder.pet.entity.Users;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsersService {
    private UsersDAO usersDAO;
    private AuthoritiesDAO authoritiesDAO;
    private static String FOLDER_CONTAIN_AVATAR_URL = "FinderPetApplication/src/main/resources/static/images/avatarUser/";
    private static String URL_AVATAR="/images/avatarUser/";
    @Autowired
    HttpSession session;

    private TwoFactorAuthPasswordsService twoFactorAuthPasswordsService;
    private FavoritesService favoritesService;

    @Autowired
    public UsersService(UsersDAO usersDAO, AuthoritiesDAO authoritiesDAO, TwoFactorAuthPasswordsService twoFactorAuthPasswordsService, FavoritesService favoritesService) {
        this.usersDAO = usersDAO;
        this.authoritiesDAO = authoritiesDAO;
        this.twoFactorAuthPasswordsService = twoFactorAuthPasswordsService;
        this.favoritesService = favoritesService;
    }

    @Transactional
    public Users createdUser(Users users) {
        Authorities authorities = new Authorities(users, "ROLE_USER");
        authorities.setId(null);
        this.authoritiesDAO.save(authorities);
        return this.usersDAO.save(users);
    }

    @Transactional
    public Optional<Users> findById(String username) {
        return this.usersDAO.findById(username);
    }

    public boolean isValidEmailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public boolean isValidPassword(String password) {
        if (this.twoFactorAuthPasswordsService.validatePatternPassword(password)) {
            return true;
        }
        return false;
    }

    public boolean isUserExists(String userName) {
        if (!this.usersDAO.findById(userName).isEmpty()){
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean changeInfoUser(Users user) {
        if(!this.usersDAO.findById(user.getUserName()).isEmpty()){
            this.usersDAO.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteFavorite(Long id) {
        if(this.favoritesService.removeOne(id)){
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteAllFavorite(String email) {
        Optional<Users> usersOptional = this.usersDAO.findById(email);
        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            List<Favorites> favorites = user.getListFavorites();

            // Sử dụng Iterator để tránh ConcurrentModificationException
            Iterator<Favorites> iterator = favorites.iterator();
            while (iterator.hasNext()) {
                Favorites favorite = iterator.next();
                this.favoritesService.removeOne(favorite.getId());
                iterator.remove(); // Xóa khỏi danh sách của user (nếu cần)
            }
            return true;
        }
        return false;
    }

    /**
     * Update Users, không thay đổi email và password
     *
     * @param file      file avatar của ngời dùng
     * @param firstName
     * @param lastName
     * @param country
     * @param address   địa chỉ
     * @param phone
     */
    @Transactional
    public void changeProfileService(MultipartFile file,
                                     String firstName,
                                     String lastName,
                                     String country,
                                     String address,
                                     String phone) throws IOException {
        Users u = usersDAO.findById(session.getAttribute("email").toString()).get();

        //Kiểm tra đã có folder chứa avatar chưa. Không có thì tạo folder
        File uploadDir = new File(FOLDER_CONTAIN_AVATAR_URL);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        if (!file.isEmpty()) {

            String fileName = file.getOriginalFilename();
            //Xoá avatar cũ trước khi tạo avatar mới
            if(!(URL_AVATAR+fileName).equals(u.getAvatar())){
                Files.delete(Paths.get(FOLDER_CONTAIN_AVATAR_URL + u.getAvatar().substring(u.getAvatar().lastIndexOf("/"))));
            }
            Path filePath = Paths.get(FOLDER_CONTAIN_AVATAR_URL + fileName);
            if (!Files.exists(filePath)) {
                Files.write(filePath, file.getBytes());
                u.setAvatar(URL_AVATAR + fileName);
                System.out.println("file co ton tai");
            }
        } else {
            System.out.println("file khong ton tai");
        }

        u.setFirstName(firstName == null || firstName.isEmpty() ? u.getFirstName() : firstName);
        u.setLastName(lastName == null || lastName.isEmpty() ? u.getLastName() : lastName);
        u.setCountry(country == null || country.isEmpty() ? u.getCountry() : country);
        u.setAddress(address == null || address.isEmpty() ? "" : address);
        u.setPhone(phone == null || phone.isEmpty() ? "" : phone);
        System.out.println("user mới: "+u.toString());
        usersDAO.save(u);
    }

    /**
     * @param currentpassword Password hiện tại
     * @param newpassword   Password mới
     * @return true nếu thành công, false nếu currentpassword không khớp với password trong database
     */
    public boolean changePasswordProfileService(String currentpassword,
                                                String newpassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Users u = usersDAO.findById(session.getAttribute("email").toString()).orElse(null);
        if(u!=null){ //check user tồn tại
            String oldPass = u.getPassword();
            boolean isPasswordMatch = passwordEncoder.matches(currentpassword, oldPass);
            if(isPasswordMatch){ //check currentpassword trùng với password trong database
                String encodedPassword = passwordEncoder.encode(newpassword);
                u.setPassword(encodedPassword);
                usersDAO.save(u);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
