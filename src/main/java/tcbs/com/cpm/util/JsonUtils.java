package tcbs.com.cpm.util;

import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static <T> T getJsonElement(JsonObject data, String name, Class<T> type) {
        return getJsonElement(data, name, type, true);
    }

    public static <T> T getJsonElement(JsonObject data, String name, Class<T> type, boolean require) {
        JsonElement elem = data.get(name);

        if (elem != null && !elem.isJsonNull()) {
            if (type == String.class) {
                return (T) String.valueOf(elem.getAsString());
            } else if (type == Integer.class) {
                return (T) Integer.valueOf(elem.getAsInt());
            } else if (type == Long.class) {
                return (T) Long.valueOf(elem.getAsLong());
            } else if (type == Double.class) {
                return (T) Double.valueOf(elem.getAsDouble());
            } else if (type == Boolean.class) {
                return (T) Boolean.valueOf(elem.getAsBoolean());
            } else if (type == JsonElement.class) {
                return (T) elem;
            } else if (type == JsonArray.class) {
                return (T) elem;
            } else if (type == JsonObject.class) {
                return (T) elem;
            }
        }

        // for trace log: the parameter is logged if null or doesn't exist
        if (require) {
            if (elem != null) {
                logger.info("Parameter {} is null", name);
            } else {
                logger.info("Don't exist parameter {}", name);
            }
        }
        return null;
    }

    public static <T> T parseJsonData(String jsonStr, Class<T> type, String source) {
        try {
            T jsonData = new Gson().fromJson(jsonStr, type);
//            if (jsonData == null) {
//                throw new CommonException(
//                        String.format("Parse data receive from %s to json <null> !!!", source), new Throwable());
//            }
            return jsonData;
        } catch (JsonSyntaxException ex) {
//            throw new CommonException(String.format(
//                    "Parse data receive from %s to json failure !!! - %s", source, ex.getMessage()), ex);
        }
        return null;
    }

    public static JsonObject parseJsonData2JsonObject(String jsonStr, String source) {
        try {
            JsonObject jsonData = new Gson().fromJson(jsonStr, JsonObject.class);
//            if (jsonData == null) {
//                throw new CommonException(
//                        String.format("Parse data receive from %s to json <null> !!!", source), new Throwable());
//            }
            return jsonData;
        } catch (JsonSyntaxException ex) {
//            throw new CommonException(String.format(
//                    "Parse data receive from %s to json failure !!! - %s", source, ex.getMessage()), ex);
        }
        return null;
    }
}
