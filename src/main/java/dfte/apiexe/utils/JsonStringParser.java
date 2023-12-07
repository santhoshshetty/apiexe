package dfte.apiexe.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonElement;

public class JsonStringParser {

	public static String REGEX_ARRAY_START = "[";
	public static String REGEX_ARRAY_END = "]";
	public static String REGEX_OBJECT_START = "{";
	public static String REGEX_OBJECT_END = "}";
	public static String REGEX_JSON_WRAPPER = "\"";

	/**
	 * This method cleans the json value
	 * 
	 * @param value
	 * @return
	 */
	public static String clean(String value,Boolean... booleans) {

		boolean trimFlag = true;

		if(booleans.length!=0)
		{
			trimFlag = booleans[0];
		}
		if (value != null) {

			if (value.startsWith(REGEX_JSON_WRAPPER)) {
				value = value.substring(value.indexOf(REGEX_JSON_WRAPPER) + 1);
				if (value.endsWith(REGEX_JSON_WRAPPER)) {
					if(trimFlag)
						value = value.substring(0, value.lastIndexOf(REGEX_JSON_WRAPPER)).trim();
					else
						value = value.substring(0, value.lastIndexOf(REGEX_JSON_WRAPPER));

				}
			}

			value = value.replaceAll("\\},\\{", "},\n{");

			return value;
		}
		else
		{
			return "";
		}
	}


	public static String getArrayHierarchy(String hierarchy) {
		return hierarchy == null ? null : hierarchy.substring(hierarchy.indexOf(REGEX_ARRAY_START) + 1, hierarchy.lastIndexOf(REGEX_ARRAY_END));
	}

	public static String getObjectHierarchy(String hierarchy) {
		return hierarchy == null ? null : hierarchy.substring(hierarchy.indexOf(REGEX_OBJECT_START) + 1, hierarchy.lastIndexOf(REGEX_OBJECT_END));
	}


	public static String getJsonKey(String hierarchy) {
		String key = null;
		if (hierarchy != null) {
			key = "";
			for (int i = 0; i < hierarchy.length(); i++) {
				if (hierarchy.charAt(i) == REGEX_ARRAY_START.charAt(0) || hierarchy.charAt(i) == REGEX_OBJECT_START.charAt(0)) {
					break;
				} else {
					key = key.concat(hierarchy.substring(i, i + 1));
				}
			}
		}
		return key;
	}


	public static String clean(String actaulValueGb)
	{
		if(actaulValueGb!=null && actaulValueGb.startsWith("\"") && actaulValueGb.endsWith("\""))
			actaulValueGb = actaulValueGb.substring(1, actaulValueGb.length()-1);

		return actaulValueGb;

	}

	public static String getValue(String sResponse, String pattern)
	{
		return getJsonValueNew(sResponse, pattern, true);
	}

	private static String getJsonValueNew(String sResponse, String pattern,Boolean... clean)
	{
		String[] splittedPattern = pattern.split("\\.");

		List<String> responces = new ArrayList<String>();

		List<String> tempResponces = new ArrayList<String>();

		responces.add(sResponse);

		int index = -1;

		for (String sCurrentJsonTag : splittedPattern) {

			tempResponces = new ArrayList<String>();

			for (String sCurrentJsonResponse : responces) {

				if(isJsonArray(sCurrentJsonResponse))
				{
					List<String> resps = Json.getStringArrayList(sCurrentJsonResponse);

					if(index==-1)
					{
						for (String string2 : resps) {
							JsonElement jsonEle = Json.getJsonElement(string2).getAsJsonObject().get(sCurrentJsonTag);
							tempResponces.add(jsonEle == null?"null":jsonEle.toString());

						}
					}
					else
					{
						tempResponces.add(Json.getJsonElement(resps.get(index)).getAsJsonObject().get(sCurrentJsonTag).toString());
						index =  -1;
					}
				}
				else if(isJsonObject(sCurrentJsonResponse))
				{
					if(sCurrentJsonTag.contains("["))
					{
						index = Integer.parseInt(sCurrentJsonTag.substring(sCurrentJsonTag.indexOf("[")+1,sCurrentJsonTag.indexOf("]")));

						sCurrentJsonTag = sCurrentJsonTag.substring(0,sCurrentJsonTag.indexOf("["));

					}
					else
					{
						index = -1;
					}

					if(index!=-1)
					{
						JsonElement jsonEle = Json.getJsonElement(sCurrentJsonResponse).getAsJsonObject().get(sCurrentJsonTag);
						List<String> resps = Json.getStringArrayList(jsonEle.toString());

						tempResponces.add(jsonEle == null?"null":resps.get(index));
					}
					else
					{
						JsonElement jsonEle = Json.getJsonElement(sCurrentJsonResponse).getAsJsonObject().get(sCurrentJsonTag);
						tempResponces.add(jsonEle == null?"null":jsonEle.toString());
					}
				}
			}

			responces.clear();
			responces.addAll(tempResponces);
		}

		String output=null;

		//System.out.println("responces-->"+responces);
		for (String string : responces) {

			if(string.startsWith("\"")&&string.endsWith("\""))
				string=string.substring(1,string.length()-1);

			if(output==null)
			{
				output=string;
			}
			else
			{
				output=output+";"+string;
			}
		}

		if(clean.length>=1)
			return clean(output);
		else
			return output;
	}

	public static boolean isJsonArray(String response)
	{
		return Json.getJsonElement(response).isJsonArray();
	}

	public static boolean isJsonObject(String response)
	{
		return Json.getJsonElement(response).isJsonObject();
	}

	public static boolean isJsonPrimitive(String response)
	{
		return Json.getJsonElement(response).isJsonPrimitive();
	}


	public static void handleDot(String pattern,List<String> elementMap ,List<String> elementMapTemp,List<String> values,int keyFlag,Boolean... booleans)
	{

		String actuKey = null;

		String indx ="";

		/* ==============  This is array ============== */

		actuKey=pattern.split("\\.")[0];
		indx=pattern.split("\\.")[1];

		elementMapTemp = new LinkedList<String>();

		for (Iterator<String> iterator = elementMap.iterator(); iterator.hasNext();)
		{
			String string = (String) iterator.next();
			if(actuKey==null)
			{
				continue;
			}
			List<String> array = Json.getStringArrayList(Json.get(string, actuKey));

			if(indx.equals("ALL"))
			{
				for (Iterator<String> it = array.iterator(); it.hasNext();) 
				{

					String jsonResp = it.next();

					if(keyFlag==1)
					{
						elementMapTemp.add(jsonResp);
					}
					else
					{
						values.add(clean(jsonResp,booleans));
					}
				}
			}
			else
			{
				if(keyFlag==1)
				{
					try {
						elementMapTemp.add(array.get(Integer.parseInt(indx)));
					} catch (Exception e) {
					}
				}
				else
				{
					try {
						values.add(clean(array.get(Integer.parseInt(indx)),booleans));
					} catch (Exception e) {
						System.out.println("Exception502 : Please also check this "+e.getMessage());
					}
				}
			}
		}

		elementMap.clear();
		elementMap.addAll(elementMapTemp);

	}
}
