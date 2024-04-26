package vn.FinderPet.FinderPetApplication.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "shelter_info")
public class ShelterInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelter_info_id")
    private Long id;

    @Column(name = "shelter_info_mission", nullable = false, columnDefinition = "LONGTEXT")
    private String shelterInfoMission;

    @Column(name = "shelter_info_policy", nullable = false, columnDefinition = "LONGTEXT")
    private String shelterInfoPolicy;

    @Column(name = "shelter_info_operating_time", nullable = false)
    private String shelterInfoOperatingTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelters shelters;

    public ShelterInfo(Long id, String shelterInfoMission, String shelterInfoPolicy, String shelterInfoOperatingTime, Shelters shelters) {
        this.id = id;
        this.shelterInfoMission = shelterInfoMission;
        this.shelterInfoPolicy = shelterInfoPolicy;
        this.shelterInfoOperatingTime = shelterInfoOperatingTime;
        this.shelters = shelters;
    }

    public ShelterInfo() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Shelters getShelters() {
        return shelters;
    }

    public void setShelters(Shelters shelters) {
        this.shelters = shelters;
    }

    @Override
    public String toString() {
        return "ShelterInfo{" +
                "id=" + id +
                ", shelterInfoMission='" + shelterInfoMission + '\'' +
                ", shelterInfoPolicy='" + shelterInfoPolicy + '\'' +
                ", shelterInfoOperatingTime='" + shelterInfoOperatingTime + '\'' +
                ", shelters=" + shelters +
                '}';
    }
}
