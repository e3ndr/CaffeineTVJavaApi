package cf.e3ndr.CaffeineJavaApi.api.Chat;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;

public class DigitalItem {
	private static HashMap<String, BufferedImage> imageCache = new HashMap<>();
	private String staticImage;
	private int count;
	private int creditsPerItem;
	
	public DigitalItem(JSONObject item) {
		this.staticImage = (String) item.get("static_image_path");
		this.count = ((Long) item.get("count")).intValue();
		this.creditsPerItem = ((Long) item.get("credits_per_item")).intValue();
	}
	
	public BufferedImage getStaticBufferedImage() {
		BufferedImage image = imageCache.get(this.staticImage);
		
		if (image == null) {
			try {
				image = ImageIO.read(new URL("https://assets.caffeine.tv" + this.staticImage));
				imageCache.put(this.staticImage, image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return image;
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
