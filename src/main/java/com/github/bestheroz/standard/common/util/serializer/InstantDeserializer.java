package com.github.bestheroz.standard.common.util.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;

public class InstantDeserializer extends StdDeserializer<Instant> {
    private static final long serialVersionUID = 2096883216046600250L;

    public InstantDeserializer() {
        this(null);
    }

    public InstantDeserializer(final Class<Instant> t) {
        super(t);
    }

    @Override
    public Instant deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException {
        return Instant.ofEpochMilli(jsonParser.getLongValue());
    }
}
