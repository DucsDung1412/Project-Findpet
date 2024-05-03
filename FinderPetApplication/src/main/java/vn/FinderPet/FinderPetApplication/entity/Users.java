package vn.FinderPet.FinderPetApplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"authorities", "userInfo", "adopterProfile", "shelters", "listSpons", "listFavorites", "listAdopt"})
public class Users {
    @Id
    @Column(name = "username", nullable = false, length = 50)
    private String userName;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Authorities authorities;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserInfo userInfo;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AdopterProfile adopterProfile;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Shelters shelters;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Spons> listSpons;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorites> listFavorites;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Adopt> listAdopt;

    public Users() {

    }

    public Users(String userName, String password, Boolean enabled, String email) {
        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Authorities getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Authorities authorities) {
        this.authorities = authorities;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AdopterProfile getAdopterProfile() {
        return adopterProfile;
    }

    public void setAdopterProfile(AdopterProfile adopterProfile) {
        this.adopterProfile = adopterProfile;
    }

    public Shelters getShelters() {
        return shelters;
    }

    public void setShelters(Shelters shelters) {
        this.shelters = shelters;
    }

    public List<Spons> getListSpons() {
        return listSpons;
    }

    public void setListSpons(List<Spons> listSpons) {
        this.listSpons = listSpons;
    }

    public List<Favorites> getListLike() {
        return listFavorites;
    }

    public void setListLike(List<Favorites> listFavorites) {
        this.listFavorites = listFavorites;
    }

    public List<Favorites> getListFavorites() {
        return listFavorites;
    }

    public void setListFavorites(List<Favorites> listFavorites) {
        this.listFavorites = listFavorites;
    }

    public List<Adopt> getListAdopt() {
        return listAdopt;
    }

    public void setListAdopt(List<Adopt> listAdopt) {
        this.listAdopt = listAdopt;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", email=" + email +
                '}';
    }
}
