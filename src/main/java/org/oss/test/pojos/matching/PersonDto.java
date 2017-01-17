package org.oss.test.pojos.matching;

import java.util.Objects;

public class PersonDto {
    private String name;
    private int age;
    private AddressDto address;

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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonDto personDto = (PersonDto) o;
        return age == personDto.age &&
                Objects.equals(name, personDto.name) &&
                Objects.equals(address, personDto.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, address);
    }
}
