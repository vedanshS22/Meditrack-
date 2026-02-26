package com.airtribe.meditrack.entity;

/**
 * SOLID (Inheritance): concrete people types like {@link Doctor} and {@link Patient}
 * share common personal attributes while inheriting identity from {@link MedicalEntity}.
 */
public abstract class Person extends MedicalEntity {

    private String name;
    private int age;
    private String phone;

    protected Person(String id, String name, int age, String phone) {
        super(id);
        this.name = name;
        this.age = age;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                '}';
    }
}

