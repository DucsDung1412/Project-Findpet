package vn.finder.pet.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.finder.pet.dao.ShelterDAO;
import vn.finder.pet.entity.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SheltersService {
    private ShelterDAO shelterDAO;
    private BreedService breedService;
    private AnimalsService animalsService;
    private AnimalInfoService animalInfoService;
    private UsersService usersService;

    @Autowired
    public SheltersService(ShelterDAO shelterDAO, BreedService breedService, AnimalsService animalsService, AnimalInfoService animalInfoService, UsersService usersService) {
        this.shelterDAO = shelterDAO;
        this.breedService = breedService;
        this.animalsService = animalsService;
        this.animalInfoService = animalInfoService;
        this.usersService = usersService;
    }

    public List<Shelters> findByShelterStatus(String awaiting) {
        return this.shelterDAO.findByShelterStatus(awaiting);
    }

    public Page<Shelters> findAll(int page, int sizePage){
        Pageable pageable = PageRequest.of(page, sizePage);
        return this.shelterDAO.findSheltersByStatusNotContaining(pageable, "Awaiting");
    }

    public Page<Shelters> findSheltersByStatusContaining(String status, int page, int sizePage){
        Pageable pageable = PageRequest.of(page, sizePage);
        return this.shelterDAO.findSheltersByStatusContaining(pageable, status);
    }

    public Page<Shelters> findByShelterNameAndShelterAddress(String status, String shelterName, String shelterAddress, int page, int sizePage){
        Pageable pageable = PageRequest.of(page, sizePage);
        return this.shelterDAO.findByShelterNameAndShelterAddress(pageable, shelterName, shelterAddress, status);
    }

    public Shelters findById(Long id){
        return this.shelterDAO.findById(id).get();
    }

    @Transactional
    public void createShelter(DtoPetShelters dto, String email) {
        Breed db = new Breed();
        db.setBreed_type(dto.getBreed_type());
        db.setBreed_name(dto.getBreed_name());
        this.breedService.save(db);

        Users users = this.usersService.findById(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Animals animals=new Animals();
        animals.setShelters(users.getShelters());
        animals.setAnimalAge(dto.getAnimalAge());
        animals.setAnimalAvatar(dto.getAnimalAvatar());
        animals.setAnimalDate(Date.valueOf(LocalDate.now()));
        animals.setAnimalGender(dto.isAnimalGender());
        animals.setAnimalName(dto.getAnimalName());
        animals.setAnimalSize(dto.getAnimalSize());
        List<Avatar> av= new ArrayList<>();
        dto.getListAvatar().forEach(e ->{
            Avatar a=new Avatar();
            a.setAvatar_name(e);
            a.setAnimals(animals);
            av.add(a);
        } );
        animals.setListAvatar(av);
        animals.setBreed(db);
        this.animalsService.save(animals);

        AnimalInfo info = new AnimalInfo();
        info.setAnimalInfoColor(dto.getAnimal_info_color());
        info.setAnimalInfoHarmony(dto.getAnimal_info_harmony());
        info.setAnimalInfoHealth(dto.getAnimal_info_health());
        info.setAnimalInfoLeg(dto.getAnimal_info_leg());
        info.setAnimalInfoCharacteristics(dto.getAnimal_info_characteristics());
        info.setAnimalInfoDescription(dto.getAnimal_info_description());
        info.setAnimals(animals);
        this.animalInfoService.save(info);
    }

    public Boolean updateShelter(Long id, String status){
        Optional<Shelters> shelters = this.shelterDAO.findById(id);
        if(!shelters.isEmpty()){
            shelters.get().setShelterStatus(status);
            this.shelterDAO.save(shelters.get());
            return true;
        }
        return false;
    }

    @Transactional
    public void addNewShelter(DtoAddShelters shelters,String email){
        Shelters sh = new Shelters();
        Users users = this.usersService.findById(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        sh.setUsers(users);
        sh.setShelterAvatar(shelters.getImg());
        sh.setShelterAddress(shelters.getLocation());
        sh.setShelterEmail(shelters.getAddressemail());
        sh.setShelterDate(Date.valueOf(LocalDate.now()));
        sh.setShelterInfoFacebook(shelters.getLinkfacebook());
        sh.setShelterInfoInstagram(shelters.getLinkintagram());
        sh.setShelterPhone(shelters.getphonenumber());
        sh.setShelterName(shelters.getNamne());
        sh.setShelterInfoOperatingTime(shelters.getOpentime());
        sh.setShelterDescription(shelters.getShortdescription());
        sh.setShelterLatitude(shelters.getLatitude());
        sh.setShelterLongitude(shelters.getLongitude());
        sh.setShelterStatus("Awaiting");
        sh.setShelterInfoPolicy(shelters.getListpolicy());
        sh.setShelterInfoMission(shelters.getListmission());

        this.shelterDAO.save(sh);
    }

    public String extractCity(String address) {
        String[] cities = {
                "An Giang", "Bà Rịa - Vũng Tàu", "Bạc Liêu", "Bắc Kạn", "Bắc Giang", "Bắc Ninh",
                "Bến Tre", "Bình Dương", "Bình Định", "Bình Phước", "Bình Thuận", "Cà Mau",
                "Cao Bằng", "Cần Thơ", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai",
                "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh", "Hải Dương",
                "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang",
                "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định",
                "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Yên", "Quảng Bình",
                "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La",
                "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang",
                "Hồ Chí Minh", "TP. Hồ Chí Minh", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
        };

        String cityPattern = String.join("|", cities);

        Pattern pattern = Pattern.compile(cityPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(address);

        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public String transStatusToVN(String status){
        switch (status){
            case "Awaiting":
                return "Đang chờ";
            case "Opening":
                return "Đã đồng ý";
            case "Canceled":
                return "Đã từ chối";
            default:
                return status;
        }
    }
}
