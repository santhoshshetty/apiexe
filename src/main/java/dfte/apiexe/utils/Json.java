package dfte.apiexe.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Json {
	public static final String DEFAULT_CHARSET = "UTF-8";

	public static String get(Object object) {
		return new GsonBuilder().serializeNulls().create().toJson(object);
	}

	public static String get(String json, String key) {
		return getJsonObject(json).get(key).toString();
	}

	public static String get(URL url, String key) {
		return getJsonObject(url).get(key).toString();
	}

	public static String getJsonString(URL url) {
		return getJsonElement(url).toString();
	}

	public static String getJsonString(String url) {
		return getJsonElement(getUrl(url)).toString();
	}

	public static JsonObject getJsonObject(String json) {
		return new JsonParser().parse(json).getAsJsonObject();
	}

	public static JsonObject getJsonObject(URL url) {
		return getJsonElement(url).getAsJsonObject();
	}

	public static JsonArray getJsonArray(String json) {
		return new JsonParser().parse(json).getAsJsonArray();
	}

	public static List<String> getStringArrayList(String json) {
		List<String> list = new ArrayList<String>();
		for (JsonElement element : getJsonArray(json))
			list.add(element.toString());
		return list;
	}

	public static List<JsonElement> getElementArrayList(String json) {
		List<JsonElement> list = new ArrayList<JsonElement>();
		for (JsonElement element : getJsonArray(json))
			list.add(element);
		return list;
	}

	public static JsonArray getJsonArray(URL url) {
		return getJsonElement(url).getAsJsonArray();
	}

	public static JsonElement getJsonElement(String json) {
		return new JsonParser().parse(json);
	}

	public static JsonElement getJsonElement(URL url) {
		JsonElement element = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), DEFAULT_CHARSET));
			element = new JsonParser().parse(in);
			in.close();
		} catch (Exception exp) {
		}
		return element;
	}

	public static URL getUrl(String url) {
		try {
			return new URL(url);
		} catch (Exception exp) {
			return null;
		}
	}
}
