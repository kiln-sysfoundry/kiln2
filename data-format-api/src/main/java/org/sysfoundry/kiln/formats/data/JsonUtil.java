package org.sysfoundry.kiln.formats.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.NonNull;

public class JsonUtil {

    public static ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();

    public static <T> T fromJSON(@NonNull String content,@NonNull Class<T> type) throws JsonProcessingException {
        ObjectReader objectReader = JSON_OBJECT_MAPPER.readerFor(type);
        T out = objectReader.readValue(content);
        return out;
    }

    public static <T> String toJSON(@NonNull T valueToSerialize,@NonNull Class<T> valueType) throws JsonProcessingException {
        ObjectWriter objectWriter = JSON_OBJECT_MAPPER.writerFor(valueType);
        String valueAsString = objectWriter.writeValueAsString(valueToSerialize);
        return valueAsString;
    }
}
