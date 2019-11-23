package cf.e3ndr.CaffeineJavaApi.api;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import cf.e3ndr.CaffeineJavaApi.api.Listener.EventListener;

public class CaffeineStream extends WebSocketClient {
	private static final String loginHeader = "{\"Headers\":{\"Authorization\":\"Anonymous CaffeineTVJavaAPI\",\"X-Client-Type\":\"api\"}}";
	private EventListener listener;
	
	public CaffeineStream(CaffeineProfile broadcaster) throws Exception {
		super(new URI("wss://realtime.caffeine.tv/v2/reaper/stages/$stage-id/messages".replace("$stage-id", broadcaster.getStageId())));
		
		this.connectBlocking();
		this.send(loginHeader);
		new KeepAlive(this).start();
	}
	
	public CaffeineStream(CaffeineProfile broadcaster, EventListener listener) throws Exception {
		this(broadcaster);
		
		this.setListener(listener);
	}
	
	public void setListener(EventListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onOpen(ServerHandshake handshake) {}
	
	@Override
	public void onMessage(String message) {
		if (!message.equals("\"THANKS\"") && (this.listener != null)) {
			this.listener.onEvent(message);
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println(reason);
	}

	@Override
	public void onError(Exception ex) {}
	
} class KeepAlive extends Thread {
	private CaffeineStream stream;
	
	public KeepAlive(CaffeineStream stream) {
		this.stream = stream;
	}
	
	@Override
	public void run() {
		while (this.stream.isOpen()) {
			try {
				this.stream.send("\"HEALZ\"");
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
		}
	}
}
