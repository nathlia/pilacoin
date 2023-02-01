package br.ufsm.poli.csi.tapw.pilacoin.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigInteger;

public class BigIntegerSerializer extends JsonSerializer<BigInteger> {
    @Override
    public void serialize(BigInteger bigInteger, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeString(bigInteger.toString());
    }
}
