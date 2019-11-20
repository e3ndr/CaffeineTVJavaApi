package cf.e3ndr.CaffeineJavaApi;

import org.json.simple.JSONObject;

import cf.e3ndr.CaffeineJavaApi.api.JsonListener;

public class TestListener implements JsonListener {

	@Override
	public void onEvent(JSONObject json) {
		System.out.println(json);
	}

}
