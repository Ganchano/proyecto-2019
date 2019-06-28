package uy.edu.ucu.jsonql2019;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class SimpleJSONHandler implements JSONHandler {
	public final JSONParser parser = new JSONParser();
	
	@Override public Object parse(String json) throws Exception {
		return toPOJO(parser.parse(json));
	}

	@Override public Object parse(Reader json) throws Exception {
		return toPOJO(parser.parse(json));
	}

	@Override public Object parse(InputStream json) throws Exception {
		Reader reader = new InputStreamReader(json);
		return parse(reader);
	}

	@Override public String stringify(Object value) throws Exception {
		return JSONValue.toJSONString(fromPOJO(value));
	}

	/** Converts from `org.json.simple` representation to the standard data types of JSONQL.
	 * @throws JSONQLRuntimeException 
	 */
	@SuppressWarnings({ "rawtypes" })
	public Object toPOJO(Object value) throws JSONQLRuntimeException {
		if (value == null || value instanceof String || value instanceof Double || value instanceof Boolean) {
			return value;
		} else if (value instanceof Long) {
			return ((Long)value).doubleValue();
		} else if (value instanceof JSONObject) {
			Map<String, Object> result = new HashMap<String, Object>();
			for (Object entryObject : ((JSONObject)value).entrySet()) {
				Map.Entry entry = (Map.Entry)entryObject;
				result.put(entry.getKey().toString(), this.toPOJO(entry.getValue()));
			}
			return result;
		} else if (value instanceof JSONArray) {
			List<Object> result = new ArrayList<Object>();
			for (Object elem : ((JSONArray)value)) {
				result.add(this.toPOJO(elem));
			}
			return result;
		} else {
			throw new JSONQLRuntimeException(); //TODO Add error message
		}
	}
	
	/** Converts from the standard JSONQL data types to `org.json.simple` value representation.
	 * @throws JSONQLRuntimeException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object fromPOJO(Object value) throws JSONQLRuntimeException {
		if (value == null || value instanceof String || value instanceof Double || value instanceof Boolean) {
			return value;
		} else if (value instanceof List) {
			JSONArray result = new JSONArray();
			result.addAll((List)value);
			return result;
		} else if (value instanceof Map) {
			JSONObject result = new JSONObject();
			result.putAll((Map)value);
			return result;
		} else {
			throw new JSONQLRuntimeException(); //TODO Add error message.
		}
	}
	
	/** This main method is for testing purposes only.
	 */
	public static void main(String[] args) throws Exception {
		SimpleJSONHandler handler = new SimpleJSONHandler();
		Object value = handler.parse("[1,true,null,{\"a\":[]}]");
		System.out.println(value);
		System.out.println(handler.stringify(value));
	}
}
