package ru.javawebinar.webapp.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonLocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {


    @Override
    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    @Override
    public LocalDate deserialize(JsonElement jsonElement, Type type,
                                 JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String str = jsonElement.getAsString();
        return LocalDate.parse(str, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
