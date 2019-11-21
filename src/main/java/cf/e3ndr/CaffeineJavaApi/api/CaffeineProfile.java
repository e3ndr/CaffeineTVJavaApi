package cf.e3ndr.CaffeineJavaApi.api;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import cf.e3ndr.CaffeineJavaApi.Networking.HttpsWebRequest;

public class CaffeineProfile {
	private boolean valid = false;
	private String username;
	private String name;
	private String caid;
	private String stageId;
	private boolean verified = false;
	private boolean featured = false;
	private int followers = 0;
	private int following = 0;
	private String bio = "";
	private String imageLink;
	
	public CaffeineProfile(String username) {
		this.username = username;
		this.refresh();
	}
	
	public CaffeineProfile(String username, JSONObject json) {
		this.username = username;
		this.set(json);
	}
	
	private void set(JSONObject json) {
		this.bio = (String) json.get("bio");
		this.name = (String) json.get("name");
		this.caid = (String) json.get("caid");
		this.stageId = (String) json.get("stage_id");
		this.username = (String) json.get("username");
		this.followers = ((Long) json.get("followers_count")).intValue();
		this.following = ((Long) json.get("following_count")).intValue();;
		this.verified = (boolean) json.get("is_verified");
		this.featured = (boolean) json.get("is_featured");
		this.imageLink = "https://images.caffeine.tv" + ((String) json.get("avatar_image_path"));
	}
	
	public void refresh() {
		String data = HttpsWebRequest.executeGet("https://api.caffeine.tv/v1/users/" + this.username).getData();
		
		try {
			if (data != null) {
				JSONObject json = (JSONObject) ((JSONObject) new JSONParser().parse(data)).get("user");
				
				this.set(json);
				
				this.valid = true;
			} else {
				this.valid = false;
			}
		} catch (Exception e) {
			this.valid = false;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.username);
		sb.append(": name=");
		sb.append(this.name);
		sb.append(", valid=");
		sb.append(this.valid);
		
		return sb.toString();
	}

	public String getName() {
		return this.name;
	}

	public String getCaid() {
		return this.caid;
	}

	public String getStageId() {
		return this.stageId;
	}

	public boolean isVerified() {
		return this.verified;
	}
	
	public boolean isValid() {
		return this.valid;
	}

	public boolean isFeatured() {
		return this.featured;
	}

	public int getFollowers() {
		return this.followers;
	}

	public int getFollowing() {
		return this.following;
	}

	public String getBio() {
		return this.bio;
	}

	public String getImageLink() {
		return this.imageLink;
	}

	public String getUsername() {
		return this.username;
	}
	
}
