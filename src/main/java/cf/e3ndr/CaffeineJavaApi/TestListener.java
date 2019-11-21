package cf.e3ndr.CaffeineJavaApi;

import cf.e3ndr.CaffeineJavaApi.api.Chat.Chat;
import cf.e3ndr.CaffeineJavaApi.api.Listener.ChatListener;

public class TestListener extends ChatListener {

	@Override
	public void onEvent(Chat chat) {
		System.out.println(chat);
	}

}
