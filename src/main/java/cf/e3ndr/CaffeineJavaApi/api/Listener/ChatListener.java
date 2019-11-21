package cf.e3ndr.CaffeineJavaApi.api.Listener;

import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONObject;

import cf.e3ndr.CaffeineJavaApi.api.CaffeineProfile;
import cf.e3ndr.CaffeineJavaApi.api.Chat.Chat;
import cf.e3ndr.CaffeineJavaApi.api.Chat.ChatType;
import cf.e3ndr.CaffeineJavaApi.api.Chat.DigitalItem;

public abstract class ChatListener implements JsonListener {
	private static HashMap<String, Chat> chats = new HashMap<>();
	
	public abstract void onEvent(Chat chat);
	
	@Override
	public void onEvent(JSONObject json) {
		String id = (String) json.get("id");
		Chat chat = chats.get(id);
		
		if (chat != null) {
			chat.setUpvotes(((Long) json.get("endorsement_count")).intValue());
		} else {
			JSONObject body = (JSONObject) json.get("body");
			JSONObject publisher = (JSONObject) json.get("publisher");
			JSONObject item = (JSONObject) body.get("digital_item");
			ChatType type = ChatType.valueOf(((String) json.get("type")).toUpperCase());
			CaffeineProfile profile = new CaffeineProfile((String) publisher.get("username"), publisher);
			DigitalItem digitalItem = null;
			
			if (item != null) digitalItem = new DigitalItem(item);
			
			chat = new Chat(profile, type, (String) body.get("text"), digitalItem);
			this.onEvent(chat);
			chats.put(id, chat);
		}
		
		this.checkChats();
	}
	
	private void checkChats() {
		Iterator<Chat> it = chats.values().iterator();
		
		while (it.hasNext()) {
			Chat chat = it.next();
			
			if ((chat.getTime() + 60000) < System.currentTimeMillis()) {
				it.remove(); // Remove a chat from the list to prevent memory leakage.
			}
		}
	}
}
