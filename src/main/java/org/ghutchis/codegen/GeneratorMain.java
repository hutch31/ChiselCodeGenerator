package org.ghutchis.codegen;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import codegen.RecordMultiplexer;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import static net.sourceforge.argparse4j.impl.Arguments.storeTrue;

public class GeneratorMain {
	public static Namespace parseArgs(String[] args) {
		Namespace res = null;
		
		ArgumentParser parser = ArgumentParsers.newFor("ChiselCodeGenerator").build()
                .description("Create multiplexer based on user provided record");
		parser.addArgument("-x").dest("xml").metavar("FILE").type(String.class).help("XML description file");
		parser.addArgument("--buffer").dest("buffer").action(storeTrue()).help("Add output buffering");
		try {
            res = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
        }
		return res;
	}
	
    public static void main(String[] args) throws IOException {
    	Namespace parsed = parseArgs(args);
    	
    	if (parsed.get("xml") == null) {
    		System.out.println("XML argument is required");
    		System.exit(0);
    	}
    	File xmlFile = new File(parsed.get("xml").toString());
    	
        byte[] encoded = Files.readAllBytes(Paths.get(xmlFile.getPath()));
        String xmlConfig = new String(encoded, StandardCharsets.UTF_8);
        RecordDef device = RecordDefFactory.unmarshalResponse(xmlConfig);

        if (device.entryList == null) {
            System.out.println("Error -- RecordDef description file must contain at least one Entry tag");
            System.exit(1);
        }

        String result = chisel3.Driver.emitVerilog(() -> {
            RecordMultiplexer conf = new RecordMultiplexer(parsed.get("buffer"), device);
            conf.suggestName(() -> device.Name.toString());
            return conf;
        });
    }
}
