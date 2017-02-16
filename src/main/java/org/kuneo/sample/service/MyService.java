package org.kuneo.sample.service;

import org.kuneo.sample.Person;
import org.springframework.stereotype.Service;

@Service
public class MyService {
  
  public String hello(Person person) {
    return "hello, " + person.getName();
  }
}
