package cc.touchuan.simulator.helper;

import java.util.HashMap;
import java.util.Map;


import com.google.gson.Gson;

public class JsonBuilder {

	private Map<String, Object> mMap = new HashMap<String, Object>();

	
	public static JsonBuilder build() {
		return new JsonBuilder();
	}
	public JsonBuilder add(String prop, Object value) {
		mMap.put(prop, value);
		
		return this;
		
	}
	

	public Object get(String prop) {
		
		return mMap.get(prop);
	}
	

	public void clear() {
		mMap.clear();
		
	}

	public String toString() {

		Gson son = new Gson();
		String jsonStr = son.toJson(mMap);
		
		return jsonStr;
	}
	
}
