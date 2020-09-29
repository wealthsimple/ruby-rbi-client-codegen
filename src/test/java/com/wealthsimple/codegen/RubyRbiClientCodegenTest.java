package com.wealthsimple.codegen;

import io.swagger.codegen.CodegenOperation;
import io.swagger.codegen.CodegenParameter;
import io.swagger.codegen.languages.RubyClientCodegen;
import io.swagger.codegen.InlineModelResolver;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RubyRbiClientCodegenTest {
    @Test(description = "handle a language defined class in body param")
    public void stringBodyParameterTest() {
        final Swagger model = parseAndPrepareSwagger("src/test/resources/petstore.json");
        final RubyRbiClientCodegen codegen = new RubyRbiClientCodegen();
        codegen.processOpts();
        final String path = "/user/{username}";
        final Operation p = model.getPaths().get(path).getGet();
        final RbiCodegenOperation op = (RbiCodegenOperation) codegen.fromOperation(path, "delete", p, model.getDefinitions());

        Assert.assertEquals(op.allParams.size(), 1);

        final RbiCodegenParameter bodyParam = (RbiCodegenParameter) op.allParams.get(0);
        Assert.assertEquals(bodyParam.rbiDataType(), "String");
    }

    @Test(description = "handle array of api-defined classes in body param")
    public void arrayCustomTypeBodyParameterTest() {
        final Swagger model = parseAndPrepareSwagger("src/test/resources/petstore.json");
        final RubyRbiClientCodegen codegen = new RubyRbiClientCodegen();
        codegen.processOpts();
        final String path = "/user/createWithList";
        final Operation p = model.getPaths().get(path).getPost();
        final RbiCodegenOperation op = (RbiCodegenOperation) codegen.fromOperation(path, "post", p, model.getDefinitions());

        Assert.assertEquals(op.allParams.size(), 1);

        final RbiCodegenParameter bodyParam = (RbiCodegenParameter) op.allParams.get(0);
        Assert.assertEquals(bodyParam.rbiDataType(), "T::Array[SwaggerClient::User]");
    }

    @Test(description = "handle array of api-defined classes in response")
    public void arrayCustomTypeResponseTest() {
        final Swagger model = parseAndPrepareSwagger("src/test/resources/petstore.json");
        final RubyRbiClientCodegen codegen = new RubyRbiClientCodegen();
        codegen.processOpts();
        final String path = "/pet/findByStatus";
        final Operation p = model.getPaths().get(path).getGet();
        final RbiCodegenOperation op = (RbiCodegenOperation) codegen.fromOperation(path, "get", p, model.getDefinitions());

        Assert.assertEquals(op.rbiReturnType(), "T::Array[SwaggerClient::Pet]");
    }

    @Test(description = "handle a language defined classes in response")
    public void stringResponseTest() {
        final Swagger model = parseAndPrepareSwagger("src/test/resources/petstore.json");
        final RubyRbiClientCodegen codegen = new RubyRbiClientCodegen();
        codegen.processOpts();
        final String path = "/user/login";
        final Operation p = model.getPaths().get(path).getGet();
        final RbiCodegenOperation op = (RbiCodegenOperation) codegen.fromOperation(path, "get", p, model.getDefinitions());

        Assert.assertEquals(op.rbiReturnType(), "String");
    }

    private static Swagger parseAndPrepareSwagger(String path) {
        Swagger swagger = new SwaggerParser().read(path);
        // resolve inline models
        new InlineModelResolver().flatten(swagger);
        return swagger;
    }
}
