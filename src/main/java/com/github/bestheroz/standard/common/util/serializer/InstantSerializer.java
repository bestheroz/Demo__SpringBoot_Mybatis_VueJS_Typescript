package com.github.bestheroz.standard.common.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;

public class InstantSerializer extends StdSerializer<Instant> {
    private static final long serialVersionUID = 2096883216046600250L;

    public InstantSerializer() {
        this(null);
    }

    public InstantSerializer(final Class<Instant> t) {
        super(t);
    }

    @Override
    public void serialize(final Instant instant, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(instant.toEpochMilli());
    }
}
