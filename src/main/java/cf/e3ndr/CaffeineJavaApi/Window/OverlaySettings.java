package cf.e3ndr.CaffeineJavaApi.Window;

import java.io.File;
import java.io.FileNotFoundException;

import org.json.simple.JSONObject;

import cf.e3ndr.CaffeineJavaApi.api.Util;

public class OverlaySettings {
	private static final String system = System.getProperty("os.name").toLowerCase();
	private File config;
	private File dir;
	private JSONObject json = null;
	
	public OverlaySettings(Overlay instance) {
		if (system.contains("win")) {
			this.dir = new File(System.getenv("APPDATA") + "/CaffeineTVJava/");
		} else if (system.contains("mac")) {
			this.dir = new File("~/Library/Application Support/CaffeineTVJava/");
		} else {
			this.dir = new File("~/CaffeineTVJava/");
		}
		
		this.dir.mkdirs();
		this.config = new File(this.dir, "settings.json");
		
		
		try {
			if (!this.config.exists()) {
				this.config.createNewFile();
			}
			
			this.json = Util.readJsonFile(this.config);
		} catch (Exception e) {
			new ErrorDialog(e.getLocalizedMessage());
			
			return;
		}
		
		int chatDisplayX = Util.getIntOrDefault(this.json, "chatDisplayX", 500);
		int chatDisplayY = Util.getIntOrDefault(this.json, "chatDisplayY", 800);
		int chatDisplayW = Util.getIntOrDefault(this.json, "chatDisplayW", 150);
		int chatDisplayH = Util.getIntOrDefault(this.json, "chatDisplayH", 200);
		instance.chatDisplay.setBounds(chatDisplayX, chatDisplayY, chatDisplayW, chatDisplayH);
		
		int donationDisplayX = Util.getIntOrDefault(this.json, "donationDisplayX", 500);
		int donationDisplayY = Util.getIntOrDefault(this.json, "donationDisplayY", 400);
		int donationDisplayW = Util.getIntOrDefault(this.json, "donationDisplayW", 200);
		int donationDisplayH = Util.getIntOrDefault(this.json, "donationDisplayH", 200);
		instance.donationDisplay.setBounds(donationDisplayX, donationDisplayY, donationDisplayW, donationDisplayH);
		
		int frameX = Util.getIntOrDefault(this.json, "frameX", 100);
		int frameY = Util.getIntOrDefault(this.json, "frameY", 100);
		int frameW = Util.getIntOrDefault(this.json, "frameW", 1280);
		int frameH = Util.getIntOrDefault(this.json, "frameH", 720);
		instance.greenModePosition = new Position(frameX, frameY, frameW, frameH);
		
		instance.checkGreenMode.setSelected(Util.getBooleanOrDefault(this.json, "greenMode", false));
		instance.checkChatDisplay.setSelected(Util.getBooleanOrDefault(this.json, "showChat", true));
		instance.checkDonationDisplay.setSelected(Util.getBooleanOrDefault(this.json, "showDonations", true));
		
		instance.caffeineStreamSelector.setText(Util.getStringOrDefault(this.json, "defaultStream", "ItzLcyx"));
		instance.blackText = Util.getBooleanOrDefault(this.json, "blackText", true);
		
	}
	
	@SuppressWarnings("unchecked")
	private void savePositionValues(Position position, String name) {
		this.json.put(name + "X", position.x);
		this.json.put(name + "Y", position.y);
		this.json.put(name + "W", position.w);
		this.json.put(name + "H", position.h);
	}
	
	@SuppressWarnings("unchecked")
	public void exit(Overlay instance) {
		this.savePositionValues(new Position(instance.chatDisplay), "chatDisplay");
		this.savePositionValues(new Position(instance.donationDisplay), "donationDisplay");
		this.savePositionValues(instance.checkGreenMode.isSelected() ? new Position(instance.frame) : instance.greenModePosition, "frame");
		
		this.json.put("greenMode", instance.checkGreenMode.isSelected());
		this.json.put("showChat", instance.checkChatDisplay.isSelected());
		this.json.put("showDonations", instance.checkDonationDisplay.isSelected());
		
		this.json.put("defaultStream", instance.caffeineStreamSelector.getText());
		this.json.put("blackText", instance.blackText);
		
		try {
			Util.writeJsonFile(this.config, json, false);
		} catch (FileNotFoundException e) {} // Rip your settings ig
	}
	
	
}
