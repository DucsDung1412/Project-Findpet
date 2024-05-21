package vn.FinderPet.FinderPetApplication.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "shelters")
@JsonIgnoreProperties({"shelterInfo", "listAnimals", "listSpons", "users"})
public class Shelters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelters_id")
    private Long id;

    @Column(name = "shelter_avatar", nullable = false)
    private String shelterAvatar;

    @Column(name = "shelter_name", nullable = false, length = 50)
    private String shelterName;

    @Column(name = "shelter_email", nullable = false)
    private String shelterEmail;

    @Column(name = "shelter_address", nullable = false)
    private String shelterAddress;

    @Column(name = "shelter_phone", nullable = false, length = 15)
    private String shelterPhone;

    @Column(name = "shelter_date", nullable = false)
    private Date shelterDate;

    @Column(name = "shelter_info_mission", nullable = false, columnDefinition = "LONGTEXT")
    private String shelterInfoMission;

    @Column(name = "shelter_info_policy", nullable = false, columnDefinition = "LONGTEXT")
    private String shelterInfoPolicy;

    @Column(name = "shelter_info_operating_time", nullable = false)
    private String shelterInfoOperatingTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "username", nullable = false)
    private Users users;

    @OneToMany(mappedBy = "shelters", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Animals> listAnimals;

    @OneToMany(mappedBy = "shelters", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Spons> listSpons;

    public Shelters(Long id, String shelterAvatar, String shelterName, String shelterEmail, String shelterAddress, String shelterPhone, Date shelterDate, Users users, String shelterInfoMission, String shelterInfoPolicy, String shelterInfoOperatingTime) {
        this.id = id;
        this.shelterAvatar = shelterAvatar;
        this.shelterName = shelterName;
        this.shelterEmail = shelterEmail;
        this.shelterAddress = shelterAddress;
        this.shelterPhone = shelterPhone;
        this.shelterDate = shelterDate;
        this.users = users;
        this.shelterInfoMission = shelterInfoMission;
        this.shelterInfoPolicy = shelterInfoPolicy;
        this.shelterInfoOperatingTime = shelterInfoOperatingTime;
    }

    public Shelters() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShelterAvatar() {
        return shelterAvatar;
    }

    public void setShelterAvatar(String shelterAvatar) {
        this.shelterAvatar = shelterAvatar;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getShelterEmail() {
        return shelterEmail;
    }

    public void setShelterEmail(String shelterEmail) {
        this.shelterEmail = shelterEmail;
    }

    public String getShelterAddress() {
        return shelterAddress;
    }

    public void setShelterAddress(String shelterAddress) {
        this.shelterAddress = shelterAddress;
    }

    public String getShelterPhone() {
        return shelterPhone;
    }

    public void setShelterPhone(String shelterPhone) {
        this.shelterPhone = shelterPhone;
    }

    public Date getShelterDate() {
        return shelterDate;
    }

    public void setShelterDate(Date shelterDate) {
        this.shelterDate = shelterDate;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getShelterInfoMission() {
        return shelterInfoMission;
    }

    public void setShelterInfoMission(String shelterInfoMission) {
        this.shelterInfoMission = shelterInfoMission;
    }

    public String getShelterInfoPolicy() {
        return shelterInfoPolicy;
    }

    public void setShelterInfoPolicy(String shelterInfoPolicy) {
        this.shelterInfoPolicy = shelterInfoPolicy;
    }

    public String getShelterInfoOperatingTime() {
        return shelterInfoOperatingTime;
    }

    public void setShelterInfoOperatingTime(String shelterInfoOperatingTime) {
        this.shelterInfoOperatingTime = shelterInfoOperatingTime;
    }

    public List<Animals> getListAnimals() {
        return listAnimals;
    }

    public void setListAnimals(List<Animals> listAnimals) {
        this.listAnimals = listAnimals;
    }

    public List<Spons> getListSpons() {
        return listSpons;
    }

    public void setListSpons(List<Spons> listSpons) {
        this.listSpons = listSpons;
    }
}
