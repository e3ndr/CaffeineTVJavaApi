package cf.e3ndr.CaffeineJavaApi.api.Chat;

import org.json.simple.JSONObject;

public class DigitalItem {
	private String staticImage;
	private int count;
	private int creditsPerItem;
	
	public DigitalItem(JSONObject item) {
		this.staticImage = (String) item.get("static_image_path");
		this.count = ((Long) item.get("count")).intValue();
		this.creditsPerItem = ((Long) item.get("credits_per_item")).intValue();
	}

	public String getStaticImage() {
		return this.staticImage;
	}

	public int getCount() {
		return this.count;
	}

	public int getCreditsPerItem() {
		return this.creditsPerItem;
	}
}
