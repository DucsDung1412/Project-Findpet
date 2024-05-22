package vn.FinderPet.FinderPetApplication.entity;

public class AnimalInfoRequest {
    private String location;
    private String breed;
    private String name;
    private String size;
    private String[] age;
    private String gender;

    public AnimalInfoRequest(String location, String breed, String name, String size, String[] age, String gender) {
        this.location = location;
        this.breed = breed;
        this.name = name;
        this.size = size;
        this.age = age;
        this.gender = gender;
    }

    public AnimalInfoRequest() {

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String[] getAge() {
        return age;
    }

    public void setAge(String[] age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getListAge(){
        String s = "[";
        for (int i = 0; i < this.getAge().length; i++){
            s += this.getAge()[i] + ", ";
        }
        s = s.trim().substring(0, s.length() - 2) + "]";
        return s;
    }

    @Override
    public String toString() {
        return "AnimalInfoRequest{" +
                "location='" + location + '\'' +
                ", breed='" + breed + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", age='" + this.getListAge() + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
