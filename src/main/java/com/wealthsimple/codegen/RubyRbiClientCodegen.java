package com.wealthsimple.codegen;

import io.swagger.codegen.languages.*;
import io.swagger.codegen.CodegenModelFactory;
import io.swagger.codegen.CodegenModelType;
import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.CodegenProperty;
import io.swagger.codegen.CodegenResponse;
import io.swagger.codegen.SupportingFile;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.properties.*;

import java.util.*;
import java.io.File;

import com.wealthsimple.codegen.RbiCodegenOperation;
import com.wealthsimple.codegen.RbiCodegenParameter;
import com.wealthsimple.codegen.RbiCodegenProperty;
import com.wealthsimple.codegen.RbiCodegenResponse;

public class RubyRbiClientCodegen extends RubyClientCodegen {
  protected String rbiFolder = "rbi";
  protected String typeFormat = ".rbi";

  public RubyRbiClientCodegen() {
    super();

    embeddedTemplateDir = templateDir = "RubyRbiClient";
    modelTemplateFiles.put("model_rbi.mustache", ".rbi");
    apiTemplateFiles.put("api_rbi.mustache", ".rbi");
    CodegenModelFactory.setTypeMapping(CodegenModelType.PROPERTY, RbiCodegenProperty.class);
    CodegenModelFactory.setTypeMapping(CodegenModelType.PARAMETER, RbiCodegenParameter.class);
    CodegenModelFactory.setTypeMapping(CodegenModelType.OPERATION, RbiCodegenOperation.class);
    CodegenModelFactory.setTypeMapping(CodegenModelType.RESPONSE, RbiCodegenResponse.class);
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

    supportingFiles.add(new SupportingFile("api_client_rbi.mustache", rbiFolder, "api_client.rbi"));
    supportingFiles.add(new SupportingFile("api_error_rbi.mustache", rbiFolder, "api_error.rbi"));
    supportingFiles.add(new SupportingFile("configuration_rbi.mustache", rbiFolder, "configuration.rbi"));
    supportingFiles.add(new SupportingFile("version_rbi.mustache", rbiFolder, "version.rbi"));
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
      return "T::Hash[String, " + getRbiTypeDeclaration(inner) + "]";
    } else if (p instanceof BooleanProperty) {
      return "T::Boolean";
    } else if (p instanceof ObjectProperty) {
      return "BasicObject";
    }

    // TODO: redo this
    StringBuilder sb = new StringBuilder("");
    String type = getTypeDeclaration(p);
    if (!languageSpecificPrimitives.contains(type)) {
      sb.append(moduleName + "::");
    }
    sb.append(type);
    return sb.toString();
  }

  @Override
  public CodegenProperty fromProperty(String name, Property p) {
    RbiCodegenProperty property = (RbiCodegenProperty) super.fromProperty(name, p);
    // the codegen property doesn't have the info about the module name
    // we need to make sure that we pass it down to the properties and all
    // the nested items for properties that correspond to containers
    property.internalClassPrefix = moduleName;
    RbiCodegenProperty items = (RbiCodegenProperty) property.items;
    while (items != null) {
      items.internalClassPrefix = moduleName;
      items = (RbiCodegenProperty) items.items;
    }

    return property;
  }

  @Override
  public CodegenResponse fromResponse(String responseCode, Response response) {
    RbiCodegenResponse codegenResponse = (RbiCodegenResponse) super.fromResponse(responseCode, response);

    if (response.getSchema() != null) {
      codegenResponse.responseProperty = (RbiCodegenProperty) fromProperty("response", response.getSchema());
    }
    return codegenResponse;
  }

  @Override
  public void postProcessParameter(CodegenParameter parameter) {
    RbiCodegenParameter castedParameter = (RbiCodegenParameter) parameter;
    castedParameter.internalClassPrefix = moduleName;
  }
}
