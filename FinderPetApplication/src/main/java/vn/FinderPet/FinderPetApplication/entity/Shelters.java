package vn.FinderPet.FinderPetApplication.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "shelters")
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

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "username", nullable = false)
    private Users users;

    @OneToOne(mappedBy = "shelters", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ShelterInfo shelterInfo;

    @OneToMany(mappedBy = "shelters", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Animals> listAnimals;

    @OneToMany(mappedBy = "shelters", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Spons> listSpons;

    public Shelters(Long id, String shelterAvatar, String shelterName, String shelterEmail, String shelterAddress, String shelterPhone, Date shelterDate, Users users) {
        this.id = id;
        this.shelterAvatar = shelterAvatar;
        this.shelterName = shelterName;
        this.shelterEmail = shelterEmail;
        this.shelterAddress = shelterAddress;
        this.shelterPhone = shelterPhone;
        this.shelterDate = shelterDate;
        this.users = users;
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

    public ShelterInfo getShelterInfo() {
        return shelterInfo;
    }

    public void setShelterInfo(ShelterInfo shelterInfo) {
        this.shelterInfo = shelterInfo;
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

    @Override
    public String toString() {
        return "Shelters{" +
                "id=" + id +
                ", shelterAvatar='" + shelterAvatar + '\'' +
                ", shelterName='" + shelterName + '\'' +
                ", shelterEmail='" + shelterEmail + '\'' +
                ", shelterAddress='" + shelterAddress + '\'' +
                ", shelterPhone='" + shelterPhone + '\'' +
                ", shelterDate=" + shelterDate +
                ", users=" + users +
                ", shelterInfo=" + shelterInfo +
                '}';
    }
}
