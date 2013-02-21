package com.topsy.jmxproxy.core;

import java.io.IOException;

import java.lang.reflect.Array;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.std.SerializerBase;

public class AttributeSerializer extends SerializerBase<Attribute> {
    public AttributeSerializer() {
        super(Attribute.class, true);
    }

    @Override
    public void serialize(Attribute value, JsonGenerator jgen, SerializerProvider providor) throws IOException, JsonProcessingException {
        serialize(jgen, value.getAttributeValue());
    }

    private void serialize(JsonGenerator jgen, Object objectValue) throws IOException, JsonProcessingException {
        if (objectValue == null) {
            jgen.writeNull();
        } else if (objectValue.getClass().isArray()) {
            jgen.writeStartArray();
            int length = Array.getLength(objectValue);
            for (int i = 0; i < length; i++) {
                serialize(jgen, Array.get(objectValue, i));
            }
            jgen.writeEndArray();
        } else if (objectValue instanceof TabularData) {
            TabularData data = (TabularData) objectValue;
            jgen.writeStartArray();
            for (Object objectEntry : data.values()) {
                serialize(jgen, objectEntry);
            }
            jgen.writeEndArray();
        } else if (objectValue instanceof CompositeData) {
            CompositeData data = (CompositeData) objectValue;
            jgen.writeStartObject();
            for (String objectEntry : data.getCompositeType().keySet()) {
                jgen.writeFieldName(objectEntry);
                serialize(jgen, data.get(objectEntry));
            }
            jgen.writeEndObject();
        } else if (objectValue instanceof Number) {
            jgen.writeNumber(((Number)objectValue).toString());
        } else if (objectValue instanceof Boolean) {
            jgen.writeBoolean((Boolean)objectValue);
        } else {
            jgen.writeString(objectValue.toString());
        }
    }
}
