package cf.e3ndr.CaffeineJavaApi.api.Listener;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public interface JsonListener extends EventListener {
	public void onEvent(JSONObject json);
	
	@Override
	default void onEvent(String raw) {
		try {
			JSONObject json = (JSONObject) new JSONParser().parse(raw);
			this.onEvent(json);
		} catch (ParseException e) {}
	}
}
