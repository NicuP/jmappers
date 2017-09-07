package org.oss.test;

import org.oss.jmappers.MapperTemplate;
import org.oss.jstub.StubFactory;
import org.oss.test.pojos.matching.Person;
import org.oss.test.pojos.matching.PersonDto;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(Config.class);
        MapperTemplate mapperTemplate = ac.getBean(MapperTemplate.class);

        Person person = StubFactory.get().createStub(Person.class);
        PersonDto personDto = mapperTemplate.map(person, PersonDto.class);
        Person personMapped2 = mapperTemplate.map(personDto, Person.class);
        if (!person.equals(personMapped2)) {
            System.err.println("bug");
        } else {
            System.out.println("okay");
        }

        PersonDto personDto1 = StubFactory.get().createStub(PersonDto.class);
        Person person1 = mapperTemplate.map(personDto1, Person.class);
        PersonDto personDtoMapped = mapperTemplate.map(person1, PersonDto.class);
        if (!personDto1.equals(personDtoMapped)) {
            System.err.println("bug");
        } else {
            System.out.println("okay");
        }
    }
}
