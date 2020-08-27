package com.wealthsimple.codegen;

import io.swagger.codegen.languages.*;
import io.swagger.codegen.SupportingFile;
import io.swagger.models.properties.*;

import java.util.*;
import java.io.File;

public class RubyRbiClientCodegen extends RubyClientCodegen {
  protected String rbiFolder = "rbi";
  protected String typeFormat = ".rbi";

  public RubyRbiClientCodegen() {
    super();

    embeddedTemplateDir = templateDir = "RubyRbiClient";
    modelTemplateFiles.put("model_rbi.mustache", ".rbi");
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

  public String rbiFileFolder() {
    return outputFolder + File.separator + rbiFolder + File.separator + gemName + File.separator + modelPackage.replace("/", File.separator);
  }

  @Override
  public String modelFilename(String templateName, String modelName) {
    String suffix = modelTemplateFiles().get(templateName);
    if (suffix.equals(typeFormat)) {
      return rbiFileFolder() + File.separator + toModelFilename(modelName) + suffix;
    } else {
      return super.modelFilename(templateName, modelName);
    }
  }
}