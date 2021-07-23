package com.quarch.configgen;

import config.RecordMultiplexer;
import org.junit.Test;

public class TestChiselBackend {
    @Test
    public void basicGenerate() {
        String basicRegister = "<RecordDef>\n" +
                "    <Name>TestRecord</Name>\n" +
                "    <Outputs>4</Outputs>\n" +
                "    <Entry>\n" +
                "        <Name>alpha</Name>\n" +
                "        <Width>4</Width>\n" +
                "    </Entry>\n" +
                "    <Entry>\n" +
                "        <Name>bravo</Name>\n" +
                "        <Width>8</Width>\n" +
                "    </Entry>\n" +
                "    <Entry>\n" +
                "        <Name>charlie</Name>\n" +
                "        <Width>3</Width>\n" +
                "    </Entry>\n" +
                "</RecordDef>";

        RecordDef ld = RecordDefFactory.unmarshalResponse(basicRegister);
        String result = chisel3.Driver.emitVerilog(() -> {
            RecordMultiplexer conf = new RecordMultiplexer(false, ld);
            conf.suggestName(() -> ld.Name.toString());
            return conf;
        });
        System.out.println(result);
    }
}
