# Dmn Extension

## Install
    mvn clean install

Add this dependency to your application pom.xml

    <dependency>
        <groupId>io.github.obscure1910</groupId>
        <artifactId>mule-dmn</artifactId>
        <version>0.2.0</version>
        <classifier>mule-plugin</classifier>
    </dependency>

## Limitation

There are several ToDos in the code.

- No complex object can be used as input
- The data structure at the output is an unknown XML or null

## Example

A sample project for AnypointStudio is in the folder "anypointStudio". The example DMN file "price.dmn" was created via VS Code
