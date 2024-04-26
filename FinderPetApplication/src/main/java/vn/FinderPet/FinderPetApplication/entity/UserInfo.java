package vn.FinderPet.FinderPetApplication.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;

@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "info_id")
    private Long id;

    @Column(name = "info_firstname", nullable = false, length = 50)
    private String firstName;

    @Column(name = "info_lastname", nullable = false, length = 50)
    private String lastName;

    @Column(name = "info_country", nullable = false, length = 50)
    private String country;

    @Column(name = "info_address")
    private String address;

    @Column(name = "info_phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "info_avatar")
    private String avatar;

    @Column(name = "info_date", nullable = false)
    private Date createdDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "username", nullable = false)
    private Users users;

    public UserInfo() {
    }

    public UserInfo(Long id, String firstName, String lastName, String country, String address, String phone, String avatar, Date createdDate, Users users) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.address = address;
        this.phone = phone;
        this.avatar = avatar;
        this.createdDate = createdDate;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createdDate=" + createdDate +
                ", users=" + users +
                '}';
    }
}
