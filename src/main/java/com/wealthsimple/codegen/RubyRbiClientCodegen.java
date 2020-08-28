package com.wealthsimple.codegen;

import io.swagger.codegen.languages.*;
import io.swagger.codegen.CodegenModelFactory;
import io.swagger.codegen.CodegenModelType;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.SupportingFile;
import io.swagger.models.properties.*;

import java.util.*;
import java.io.File;

import com.wealthsimple.codegen.RbiCodegenProperty;

public class RubyRbiClientCodegen extends RubyClientCodegen {
  protected String rbiFolder = "rbi";
  protected String typeFormat = ".rbi";

  public RubyRbiClientCodegen() {
    super();

    embeddedTemplateDir = templateDir = "RubyRbiClient";
    modelTemplateFiles.put("model_rbi.mustache", ".rbi");
    apiTemplateFiles.put("api_rbi.mustache", ".rbi");
    CodegenModelFactory.setTypeMapping(CodegenModelType.PROPERTY, RbiCodegenProperty.class);
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

  public String rbiClientFileFolder() {
    return outputFolder + File.separator + rbiFolder + File.separator + gemName;
  }

  public String modelRbiFileFolder() {
    return rbiClientFileFolder() + File.separator + modelPackage().replace("/", File.separator);
  }

  public String apiRbiFileFolder() {
    return rbiClientFileFolder() + File.separator + apiPackage().replace("/", File.separator);
  }

  @Override
  public String modelFilename(String templateName, String modelName) {
    String suffix = modelTemplateFiles().get(templateName);
    if (suffix.equals(typeFormat)) {
      return modelRbiFileFolder() + File.separator + toModelFilename(modelName) + suffix;
    } else {
      return super.modelFilename(templateName, modelName);
    }
  }

  @Override
  public String apiFilename(String templateName, String tag) {
    String suffix = apiTemplateFiles().get(templateName);
    if (suffix.equals(typeFormat)) {
      return apiRbiFileFolder() + File.separator + toApiFilename(tag) + suffix;
    } else {
      return super.apiFilename(templateName, tag);
    }
  }

  public String getRbiTypeDeclaration(Property p) {
    if (p instanceof ArrayProperty) {
      ArrayProperty ap = (ArrayProperty) p;
      Property inner = ap.getItems();
      return "T::Array[" + getRbiTypeDeclaration(inner) + "]";
    } else if (p instanceof MapProperty) {
      MapProperty mp = (MapProperty) p;
      Property inner = mp.getAdditionalProperties();
      return "T::Hash<String, " + getRbiTypeDeclaration(inner) + "]";
    } else if (p instanceof BooleanProperty) {
      return "T::Boolean";
    } else if (p instanceof ObjectProperty) {
      return "BasicObject";
    }

    return getTypeDeclaration(p);
  }

  @Override
  public CodegenProperty fromProperty(String name, Property p) {
    RbiCodegenProperty property = (RbiCodegenProperty) super.fromProperty(name, p);
    property.rbiDataType = getRbiTypeDeclaration(p);

    return property;
  }
}