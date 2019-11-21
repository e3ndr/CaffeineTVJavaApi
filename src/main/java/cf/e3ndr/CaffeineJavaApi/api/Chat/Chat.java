package cf.e3ndr.CaffeineJavaApi.api.Chat;

import cf.e3ndr.CaffeineJavaApi.api.CaffeineProfile;

public class Chat {
	private CaffeineProfile sender;
	private ChatType type;
	private String text;
	private long time = System.currentTimeMillis();
	private int upvotes = 0;
	private DigitalItem digitalItem;
	
	public Chat(CaffeineProfile sender, ChatType type, String text, DigitalItem digitalItem) {
		this.sender = sender;
		this.type = type;
		this.text = text;
		this.digitalItem = digitalItem;
	}
	
	public String getText() {
		return this.text;
	}
	
	public CaffeineProfile getSender() {
		return this.sender;
	}
	
	public ChatType getType() {
		return this.type;
	}

	public long getTime() {
		return this.time;
	}

	public int getUpvotes() {
		return this.upvotes;
	}

	public void setUpvotes(int upvotes) {
		this.upvotes = upvotes;
	}
	
	public DigitalItem getDigitalItem() {
		return this.digitalItem;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.sender.getUsername());
		sb.append(": ");
		sb.append(this.text);
		
		return sb.toString();
	}
}
