# Swagger Codegen Module for Ruby with RBI support

## Overview
This is a custom Swagger Codegen Module that can be used to generate the Ruby client together with the corresponding RBI files that can be used by Sorbet for the purposes of the type checking inside the library consumer's project.

## What's Swagger?
The goal of Swagger™ is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection. When properly defined via Swagger, a consumer can understand and interact with the remote service with a minimal amount of implementation logic. Similar to what interfaces have done for lower-level programming, Swagger removes the guesswork in calling the service.

Check out [OpenAPI-Spec](https://github.com/OAI/OpenAPI-Specification) for additional information about the Swagger project, including additional libraries with support for other languages and more.

## What is Sorbet/RBI files?
### Sorbet
[Sorbet](https://sorbet.org/) is a gradual type checker for Ruby. Sorbet adds a way for its users to annotate the methods with signatures or `sig`s that would provide the details of the types of the parameters that the method is expecting and its return type. Those `sig`s are used for the static and runtime type checking of the codebase. Check out [Sorbet Docs](https://sorbet.org/docs/overview) for additional details.

### RBI
RBI or Ruby Interface files are used by Sorbet to learn about constants, ancestors, and methods defined in a ways it doesn't understand natively. They look just the same as normal Ruby files, except that method definitions have no implementations.
The most common use for RBI files is to define classes, constants and methods for Sorbet.

Check out [Sorbet's RBI docs](https://sorbet.org/docs/rbi) for more details and examples.

## How does this project fit into the Sorbet eco-system?
Currently, RBI files can be auto-generated using runtime reflection (either through `srb rbi gems` or through tools like [Tapioca](https://github.com/shopify/tapioca)). The drawback of the existing auto-generating tools is that they have no way of infering the types of the parameters and the return types of the methods inside the gem.

That's exactly the area that this project is addressing: for each of the ruby files there is a corresponding RBI file that is automatically generated. These RBI files will automatically be picked up by Sorbet. If the consumer of this gem is not using the Sorbet, then the generated RBI files will be ignored and it will behave just like a gem that was generated using the regular Ruby codegen module that ships with Swagger.

### Why not add sigs to the generated source files?
While it would allow us to generate a library that is better typed using some of the datastructures provided by Sorbet (such as T::Struct and T::Enum), but it would then could only work with projects that already use Sorbet. We wanted the generated library to be usable by both the projects that have Sorbet in their Gemfiles and the ones that don't, so it could be deployed across the org and have exactly the same code.

## Usage
### Prerequisites
You will need to get Swagger Codegen CLI jar. The easiest way to do it is by running `curl https://oss.sonatype.org/content/repositories/releases/io/swagger/swagger-codegen-cli/2.4.15/swagger-codegen-cli-2.4.15.jar -L -o swagger-codegen-cli.jar`, which will download the jar for you


### Generating the gem from OpenAPI Spec 
Get the compiled JAR file from Maven central repository (TODO, as we don't publish it yet)

Once you have downloaded it to generate the gem you can simply run:
```
java -cp path/to/rubyRbiClient-swagger-codegen-1.0.0.jar:/path/to/swagger-codegen-cli.jar io.swagger.codegen.SwaggerCodegen generate -i /path/to/api.json -l ruby-rbi -o /path/to/output-gem
```

This will generate the gem source code in `/path/to/output-gem` for you based on the spec provided by `/path/to/api.json`

## How to build and debug locally
The project is using Maven for build automation

### Building the jar locally
The following commands should build the jar for you locally:
```
git clone git@github.com:wealthsimple/ruby-rbi-client-codegen.git
cd ruby-rbi-client-codegen
mvn clean package
```

At the end of the build process you should have `target/rubyRbiClient-swagger-codegen-1.0.0.jar` with the compiled module.

### Running tests locally
```
mvn test
```