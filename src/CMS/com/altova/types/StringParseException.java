/**
 * StringParseException.java
 *
 * This file was generated by MapForce 2015r4sp1.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the MapForce Documentation for further details.
 * http://www.altova.com/mapforce
 */


package com.altova.types;


public class StringParseException extends SchemaTypeException {
  int position;

  public StringParseException(String text, int newposition) {
    super(text);
    position = newposition;
  }

  public StringParseException(Exception other) {
    super(other);
  }
}
