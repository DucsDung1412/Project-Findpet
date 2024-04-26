package vn.FinderPet.FinderPetApplication.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "breed")
public class Breed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "breed_id")
    private Long id;

    @Column(name = "breed_type", nullable = false, length = 50)
    private String breed_type;

    @Column(name = "breed_name", nullable = false, length = 50)
    private String breed_name;

    @OneToOne(mappedBy = "breed", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Animals animals;

    public Breed(Long id, String breed_type, String breed_name) {
        this.id = id;
        this.breed_type = breed_type;
        this.breed_name = breed_name;
    }

    public Breed() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBreed_type() {
        return breed_type;
    }

    public void setBreed_type(String breed_type) {
        this.breed_type = breed_type;
    }

    public String getBreed_name() {
        return breed_name;
    }

    public void setBreed_name(String breed_name) {
        this.breed_name = breed_name;
    }

    public Animals getAnimals() {
        return animals;
    }

    public void setAnimals(Animals animals) {
        this.animals = animals;
    }

    @Override
    public String toString() {
        return "Breed{" +
                "id=" + id +
                ", breed_type='" + breed_type + '\'' +
                ", breed_name='" + breed_name + '\'' +
                '}';
    }
}
