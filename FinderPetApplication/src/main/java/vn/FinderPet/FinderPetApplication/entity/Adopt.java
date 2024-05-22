package vn.FinderPet.FinderPetApplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "adopt")
@JsonIgnoreProperties({"users", "animals"})
public class Adopt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopt_id")
    private Long id;

    @Column(name = "adopt_date", nullable = false)
    private Date adoptDate;

    @Column(name = "adopt_fee", nullable = false)
    private Double adopt_fee;

    @Column(name = "adopt_pay", nullable = false)
    private Boolean adopt_pay;

    @Column(name = "adopt_status", nullable = false)
    private String adopt_status;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private Users users;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "animals_id", nullable = false)
    private Animals animals;

    public Adopt(Long id, Date adoptDate, Double adopt_fee, Boolean adopt_pay, String adopt_status, Users users, Animals animals) {
        this.id = id;
        this.adoptDate = adoptDate;
        this.adopt_fee = adopt_fee;
        this.adopt_pay = adopt_pay;
        this.adopt_status = adopt_status;
        this.users = users;
        this.animals = animals;
    }

    public Adopt() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAdoptDate() {
        return adoptDate;
    }

    public void setAdoptDate(Date adoptDate) {
        this.adoptDate = adoptDate;
    }

    public Double getAdopt_fee() {
        return adopt_fee;
    }

    public void setAdopt_fee(Double adopt_fee) {
        this.adopt_fee = adopt_fee;
    }

    public Boolean getAdopt_pay() {
        return adopt_pay;
    }

    public void setAdopt_pay(Boolean adopt_pay) {
        this.adopt_pay = adopt_pay;
    }

    public String getAdopt_status() {
        return adopt_status;
    }

    public void setAdopt_status(String adopt_status) {
        this.adopt_status = adopt_status;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Animals getAnimals() {
        return animals;
    }

    public void setAnimals(Animals animals) {
        this.animals = animals;
    }

    @Override
    public String toString() {
        return "Adopt{" +
                "id=" + id +
                ", adoptDate=" + adoptDate +
                ", adopt_fee=" + adopt_fee +
                ", adopt_pay=" + adopt_pay +
                ", adopt_status='" + adopt_status + '\'' +
                ", users=" + users +
                ", animals=" + animals +
                '}';
    }
}
