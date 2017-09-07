package org.oss.test.service;


import org.oss.jmappers.Mapper;
import org.oss.test.pojos.matching.AddressDto;
import org.oss.test.pojos.matching.Person;
import org.oss.test.pojos.matching.PersonDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Mapper
public class ServiceWithFunction implements Function<Person, PersonDto> {



    public PersonDto apply(Person person) {
        PersonDto personDto = new PersonDto();
        personDto.setAge(person.getAge());
        personDto.setName(person.getName());

        AddressDto addressDto = new AddressDto();
        addressDto.setNo(person.getAddress().getNo());
        addressDto.setStreet(person.getAddress().getStreet());
        personDto.setAddress(addressDto);
        return personDto;
    }
}
