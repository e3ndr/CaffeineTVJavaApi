package cf.e3ndr.CaffeineJavaApi.Window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import cf.e3ndr.CaffeineJavaApi.api.Chat.Chat;
import cf.e3ndr.CaffeineJavaApi.api.Listener.ChatListener;
import javax.swing.JTextArea;

public class Overlay extends ChatListener {
	private JFrame frame = new JFrame();
	private ChatDisplay chatDisplay = new ChatDisplay();
	private JToolBar toolBar = new JToolBar();
	private boolean visible = true;
	
	public static void main(String[] args) {
		new Overlay();
	}

	public Overlay() {
		this.initialize();
		this.frame.setVisible(true);
	}
	
	private void initialize() {
		// Dimension window = Toolkit.getDefaultToolkit().getScreenSize();
		
		this.frame.setBounds(100, 100, 450, 300);
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// this.frame.setUndecorated(true);
		// this.frame.setBackground(new Color(0, 0, 0, 0));
		this.frame.setFocusable(false);
		this.frame.setAlwaysOnTop(true);
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setOpaque(false);
		desktopPane.setBackground(new Color(0, 0, 0, 0));
		this.frame.getContentPane().add(desktopPane, BorderLayout.CENTER);
		
		this.initChatDisplay(desktopPane);
		this.initToolBar(desktopPane);
		
	}
	
	private void toggleDisplay() {
		this.visible = !this.visible;
		
		this.toolBar.setVisible(this.visible);
		this.chatDisplay.setTitleBar(this.visible);
		
	}
	
	private void initToolBar(JDesktopPane pane) {
		this.toolBar.setBounds(0, 0, 424, 33);
		this.toolBar.setOpaque(false);
		pane.add(this.toolBar);
		
		
		JCheckBox checkChatDisplay = new JCheckBox("Chat Display");
		checkChatDisplay.setSelected(true);
		checkChatDisplay.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				chatDisplay.setVisible(checkChatDisplay.isSelected());
			}
		});
		this.toolBar.add(checkChatDisplay);
	}
	
	private void initChatDisplay(JDesktopPane pane) {
		pane.setLayer(this.chatDisplay, 0);
		
		pane.add(chatDisplay);
		
		this.chatDisplay.setVisible(true);
		
	}
	
	@Override
	public void onEvent(Chat chat) {
		
	}
} class ChatDisplay extends JInternalFrame {
	private BasicInternalFrameTitlePane title  = (BasicInternalFrameTitlePane) ((BasicInternalFrameUI) this.getUI()).getNorthPane();
	JTextArea textArea = new JTextArea();
	
	
	public ChatDisplay() {
		super("Chat Display");
		this.setResizable(true);
		this.setBounds(178, 63, 151, 187);
		this.remove(this.title);
		this.setTitleBar(true);
		this.setFrameIcon(null);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(0, 0, 0, 0), new Color(0, 0, 0, 0)));
		
		this.textArea.setEditable(false);
		this.textArea.setLineWrap(true);
		this.textArea.setOpaque(false);
		this.textArea.setText("Testing\n1\n2\n3");
		this.getContentPane().add(textArea, BorderLayout.CENTER);
		
	}
	
	public void setTitleBar(boolean show) {
		if (show) {
			this.add(this.title, BorderLayout.NORTH);
		} else {
			this.remove(this.title);
		}
	}
	
}
