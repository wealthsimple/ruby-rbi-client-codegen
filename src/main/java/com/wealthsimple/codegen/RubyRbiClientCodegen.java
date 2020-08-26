package com.wealthsimple.codegen;

import io.swagger.codegen.languages.*;
import io.swagger.models.properties.*;

import java.util.*;
import java.io.File;

public class RubyRbiClientCodegen extends RubyClientCodegen {
  /**
   * Configures a friendly name for the generator.  This will be used by the generator
   * to select the library with the -l flag.
   *
   * @return the friendly name for the generator
   */
  @Override
  public String getName() {
    return "ruby-rbi";
  }
}