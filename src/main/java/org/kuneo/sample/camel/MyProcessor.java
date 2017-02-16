package org.kuneo.sample.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.kuneo.sample.Person;
import org.kuneo.sample.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;

public class MyProcessor implements Processor {

  @Autowired
  MyService service;

  @Override
  public void process(Exchange exchange) throws Exception {
    // csvからPersonクラスのリストに変換
    Person person = exchange.getIn().getBody(Person.class);
    exchange.getIn().setBody(service.hello(person));
  }
}
