package org.oss.test.service;

import org.oss.jmappers.Mapper;
import org.oss.jmappers.MappingMethod;
import org.oss.test.pojos.matching.Address;
import org.oss.test.pojos.matching.Person;
import org.oss.test.pojos.matching.PersonDto;
import org.springframework.stereotype.Service;

@Service
@Mapper
public class ServiceWithAnnotationMethod {

    public void sampleVoid() {
        int a = 2 + 3;
    }

    public String sampleReturn() {
        return "";
    }

    public void sampleAccept(String s) {
        int a = 2 + 3;
    }

    @MappingMethod
    public Person map(PersonDto personDto) {
        Person person = new Person();
        person.setAge(personDto.getAge());
        person.setName(personDto.getName());

        Address address = new Address();
        address.setNo(personDto.getAddress().getNo());
        address.setStreet(personDto.getAddress().getStreet());
        person.setAddress(address);
        return person;
    }
}
