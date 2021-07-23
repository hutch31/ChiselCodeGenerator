# ChiselCodeGenerator

This is a template for a Chisel-based code generator with a Java front end.

## Background

Many ASIC and FPGA design projects end up with chunks of repetitive code which
is better generated from a script or tool than hand written.  This project
provides a template for writing a custom code generation tool using Chisel as
the back-end code generator and Java as the front-end processing language.

## Description

In the template the front-end simply parses command-line arguments to read
in an XML file containing a "custom record" definition, uses the Java JAXB
parser to create a Java object from the XML, and then passes the object to
Chisel to perform the code generation.

The tool also takes a command-line argument and passes it in directly,
showing an alternate way of controlling code generation.

The tool builds using Maven to create a self-contained JAR file which could
then be distributed to team members.

## Usage

1)  Build the JAR file

    "mvn package"
2)  Create an input record format

    The XML file needs to contain a RecordDef tag with one or more Entry tags.
    See example directory.
3)  Execute the tool

    "java -jar target/ChiselCodeGenerator-0.1-jar-with-dependencies.jar -x examples/TestRecord.xml"

The tool creates a single Verilog file named per the Name tag in RecordDef.
