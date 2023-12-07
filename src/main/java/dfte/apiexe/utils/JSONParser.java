package dfte.apiexe.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * Class offers utility methods to parse JSON string to various outputs
 * 
 * @author u775090
 *
 */
public class JSONParser {

	public static String toJSON(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}

	/**
	 * This method accepts json string value and returns object of class
	 * 
	 * @param json
	 * @return
	 * @throws JsonSyntaxException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static <T> T parseJSON(String json, Class<T> cls) {

		Gson gson = new Gson();
		T returnVal = gson.fromJson(json, cls);
		return returnVal;
	}

	/**
	 * Parse a json to integer - use if json is returning a count or number (Use
	 * case - number of bucket ids)
	 * 
	 * @param json
	 * @param sMemberName
	 * @return
	 */
	public static Integer parseJSONToInt(String json, String sMemberName) {

		JsonParser jParser = new JsonParser();
		JsonObject jObj = jParser.parse(json).getAsJsonObject();
		return jObj.get(sMemberName).getAsInt();
	}

	/**
	 * Parse a json string to a list of String - use if response is returning a list
	 * of string objects Use case - get-ids
	 * 
	 * @param json
	 * @return
	 */
	public static List<String> parseJSONToArray(String json) {

		JsonParser jParser = new JsonParser();

		// Adding to handle the malformed json
		/*
		 * JsonReader jReader = new JsonReader(new StringReader(json));
		 * jReader.setLenient(true); JsonArray jObj =
		 * jParser.parse(jReader).getAsJsonArray();
		 */

		JsonArray jObj = jParser.parse(json).getAsJsonArray();
		List<String> lstOfValues = new ArrayList<String>();
		for (JsonElement j : jObj) {
			lstOfValues.add(j.getAsString());
		}
		return lstOfValues;
	}

	/**
	 * Parses multiple json docs and returns a list of objects of type specified by
	 * class Method doesn't remove blob from result
	 * 
	 * @param json - json response
	 * @param c    - collection to fetch from
	 * @param cls  - If a specific class needs to be cast to
	 * @return
	 */
	public static <T> List<T> parseJSONDocs(String json, Class<T> cls) {
		// Strip off blob if present
		List<T> lstDocs = new ArrayList<T>();
		if (!json.equals("[]")) {
			JsonParser jParser = new JsonParser();
			JsonArray j = jParser.parse(json).getAsJsonArray();
			for (int i = 0; i < j.size(); i++) {
				lstDocs.add(parseJSON(j.get(i).getAsJsonObject(), cls));
			}
		} else {
			if (json.equals("[]"))
				return null;
		}
		return lstDocs;
	}

	/**
	 * Parses multiple json docs and returns a list of objects of type specified by
	 * class Method doesn't remove blob from result
	 * 
	 * @param json - json response
	 * @param c    - collection to fetch from
	 * @param cls  - If a specific class needs to be cast to
	 * @return
	 */
	public static <T> T parseJSONDoc(String json, Class<T> cls) {
		// Strip off blob if present
		if (!json.equals("[]")) {
			JsonParser jParser = new JsonParser();
			JsonArray j = jParser.parse(json).getAsJsonArray();
			return parseJSON(j.get(0).getAsJsonObject(), cls);
		} else {
			return null;
		}
	}

	/**
	 * Parse a jsonelement to a class
	 * 
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> T parseJSON(JsonElement json, Class<T> cls) {
		Gson gson = new Gson();
		T returnVal = gson.fromJson(json, cls);

		return returnVal;
	}

	/**
	 * Returns jsonobject for root level nodes eg. _blob, _ft, _search
	 * 
	 * @param json
	 * @param objectToFetch
	 * @return
	 */
	public static JsonObject parseJSONIndexes(String json, String objectToFetch) {

		// Strip off blob if present
		if (json.contains(objectToFetch)) {
			JsonParser jParser = new JsonParser();
			JsonArray j = jParser.parse(json).getAsJsonArray();
			return parseJSONSpecificObject(j.get(0), objectToFetch);
//			return  j.get(0).getAsJsonObject().get(objectToFetch).getAsJsonObject();
		} else {
			return null;
		}
	}

	/**
	 * Returns jsonobject for root level nodes eg. _blob, _ft, _search
	 * 
	 * @param json
	 * @param objectToFetch
	 * @return
	 */
	public static JsonObject parseJSONSpecificObject(JsonElement json, String objectToFetch) {

		if (json.toString().contains("\"" + objectToFetch + "\"")) {
			JsonObject j = json.getAsJsonObject();
			return j.get(objectToFetch).getAsJsonObject();
		} else {
			return null;
		}
	}

	public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
		try {
			T[] arr = new Gson().fromJson(s, clazz);
			return Arrays.asList(arr);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}
}
