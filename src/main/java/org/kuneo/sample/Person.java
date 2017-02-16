package org.kuneo.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.FixedLengthRecord;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CsvRecord(separator = ",", skipFirstLine = false)
@FixedLengthRecord(ignoreTrailingChars = true)
public class Person {
  
  @DataField(pos = 2)
  private int age;
  
  @DataField(pos = 1)
  private String name;
}
