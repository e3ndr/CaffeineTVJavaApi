package cf.e3ndr.CaffeineJavaApi.window;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import cf.e3ndr.CaffeineJavaApi.api.CaffeineProfile;
import cf.e3ndr.CaffeineJavaApi.api.CaffeineStream;
import cf.e3ndr.CaffeineJavaApi.api.Chat.Chat;
import cf.e3ndr.CaffeineJavaApi.api.Listener.ChatListener;

public class ChatDisplay extends ChatListener {
	private JFrame frame;
	private JTextArea textPane;
	
	public ChatDisplay(CaffeineProfile profile) {
		initialize(profile.getUsername());
		try {
			new CaffeineStream(profile, this);
		} catch (Exception e) {}
		this.frame.setVisible(true);
	}
	
	private void initialize(String username) {
		frame = new JFrame();
		frame.setTitle(username + " - Stream chat");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textPane = new JTextArea();
		textPane.setWrapStyleWord(true);
		textPane.setEditable(false);
		textPane.setLineWrap(true);
		textPane.setBackground(new Color(240, 240, 240, 240));
		frame.getContentPane().add(textPane, BorderLayout.SOUTH);
	}

	@Override
	public void onEvent(Chat chat) {
		textPane.setText(textPane.getText() + chat.toString() + "\n");
	}

}
