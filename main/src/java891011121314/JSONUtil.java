package java891011121314;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class JSONUtil {

    /** Types that may be named in polymorphic type ids: this project's packages plus JDK collections/time/boxed types. */
    public static final PolymorphicTypeValidator TYPE_VALIDATOR = BasicPolymorphicTypeValidator.builder()
            .allowIfSubType("java.util.")
            .allowIfSubType("java.time.")
            .allowIfSubType("java.lang.")
            .allowIfSubType("java891011121314.")
            .allowIfSubType("rfs.")
            .allowIfSubType("fargate.")
            .allowIfSubType("generalgraph.")
            .allowIfSubType("fs.")
            .allowIfSubTypeIsArray()
            .build();

    static ObjectMapper mapper = new ObjectMapper();


    static {
        mapper.registerModule(new JavaTimeModule());
        // Default typing keeps the existing JSON format (type ids for Object / abstract fields),
        // but only for an allowlist of types. Unrestricted default typing lets crafted JSON
        // instantiate arbitrary classpath "gadget" classes (remote code execution).
        mapper.activateDefaultTyping(TYPE_VALIDATOR);
    }



    public static String toJSON(Object object)
    {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Object fromJSON(String json, Class<?> clazz)
    {
        try {
            return mapper.readValue(json,clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String toPrettyJSON(Object object)
    {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }


}
