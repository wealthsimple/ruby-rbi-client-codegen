package com.wealthsimple.codegen;

import io.swagger.codegen.languages.*;
import io.swagger.codegen.SupportingFile;
import io.swagger.models.properties.*;

import java.util.*;
import java.io.File;

public class RubyRbiClientCodegen extends RubyClientCodegen {
  protected String rbiFolder = "rbi";

  public RubyRbiClientCodegen() {
    super();

    embeddedTemplateDir = templateDir = "RubyRbiClient";
  }

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

  @Override
  public void processOpts() {
    super.processOpts();

    supportingFiles.add(new SupportingFile("api_error_rbi.mustache", rbiFolder, "api_error.rbi"));
  }
}
