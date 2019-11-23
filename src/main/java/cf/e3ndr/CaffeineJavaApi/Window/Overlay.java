package cf.e3ndr.CaffeineJavaApi.Window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import cf.e3ndr.CaffeineJavaApi.api.CaffeineProfile;
import cf.e3ndr.CaffeineJavaApi.api.CaffeineStream;
import cf.e3ndr.CaffeineJavaApi.api.Chat.Chat;
import cf.e3ndr.CaffeineJavaApi.api.Listener.ChatListener;
import java.awt.GridLayout;
import javax.swing.JTextField;

public class Overlay extends ChatListener implements NativeKeyListener {
	private static Overlay instance;
	private JFrame frame = new JFrame();
	private ChatDisplay chatDisplay = new ChatDisplay();
	private JInternalFrame toolBar = new JInternalFrame();
	private boolean visible = true;
	private CaffeineStream stream;
	
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);
		logger.setUseParentHandlers(false);
		
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(new Overlay());
	}

	public Overlay() {
		instance = this;
		this.initialize();
		this.frame.setVisible(true);
	}
	
	private void initialize() {
		int width = 0;
		int height = 0;
		int x = 0;
		int y = 0;
		
		for (GraphicsDevice screen : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			DisplayMode mode = screen.getDisplayMode();
			Rectangle config = screen.getDefaultConfiguration().getBounds();
			
			width += mode.getWidth();
			height += mode.getHeight();
			
			if (config.x < x) {
				x -= config.x;
			}
			
			if (config.y < y) {
				y += config.y;
			}
			
		}
		
		this.frame.setBounds(x, y, width, height);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setFocusable(false);
		this.frame.setAlwaysOnTop(true);
		this.frame.setUndecorated(true);
		this.frame.setBackground(new Color(0, 0, 0, 0));
		
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
		this.toolBar.setTitle("Toolbar");
		this.toolBar.setBounds(750, 600, 500, 50);
		this.toolBar.setFrameIcon(null);
		this.toolBar.setResizable(true);
		this.toolBar.setOpaque(false);
		this.toolBar.getContentPane().setLayout(new GridLayout(0, 4, 0, 0));
		
		JTextField caffeineStreamSelector = new JTextField();
		caffeineStreamSelector.setText("Slatsss");
		
		JButton btnChangeStream = new JButton("Change Stream");
		btnChangeStream.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (stream != null) stream.close();
				chatDisplay.textArea.setText("");
				
				try {
					stream = new CaffeineStream(new CaffeineProfile(caffeineStreamSelector.getText()), instance);
				} catch (Exception ex) {}
			}
		});
		
		JCheckBox checkChatDisplay = new JCheckBox("Chat Display");
		checkChatDisplay.setSelected(true);
		checkChatDisplay.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				chatDisplay.setVisible(checkChatDisplay.isSelected());
			}
		});
		
		JButton btnCloseUI = new JButton("Close UI");
		btnCloseUI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleDisplay();
			}
		});
		
		this.toolBar.getContentPane().add(btnCloseUI);
		this.toolBar.getContentPane().add(checkChatDisplay);
		this.toolBar.getContentPane().add(caffeineStreamSelector);
		this.toolBar.getContentPane().add(btnChangeStream);
		
		this.toolBar.setVisible(true);
		pane.add(this.toolBar);
	}
	
	private void initChatDisplay(JDesktopPane pane) {
		pane.add(this.chatDisplay);
		this.chatDisplay.setBackground(new Color(0, 0, 0, 0));
		this.chatDisplay.setOpaque(false);
		this.chatDisplay.setVisible(true);
		
	}
	
	@Override
	public void onEvent(Chat chat) {
		this.chatDisplay.textArea.setText(this.chatDisplay.textArea.getText() + chat.toString() + "\n");
		this.chatDisplay.repaint();
		
	}
	
	
	private boolean alt = false;
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Left Alt")) {
			this.alt = true;
		} else if (this.alt && NativeKeyEvent.getKeyText(e.getKeyCode()).equals("C")) {
			this.toggleDisplay();
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Left Alt")) {
			this.alt = false;
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {}
	
} @SuppressWarnings("serial") class ChatDisplay extends JInternalFrame {
	private BasicInternalFrameTitlePane title  = (BasicInternalFrameTitlePane) ((BasicInternalFrameUI) this.getUI()).getNorthPane();
	JTextArea textArea = new JTextArea();
	
	
	public ChatDisplay() {
		super("Chat Display");
		this.setResizable(true);
		this.setBounds(500, 500, 150, 200);
		this.remove(this.title);
		this.setTitleBar(true);
		this.setFrameIcon(null);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(0, 0, 0, 0), new Color(0, 0, 0, 0)));
		this.setBackground(new Color(0, 0, 0, 0));
		this.setOpaque(false);
		
		this.textArea.setEditable(false);
		this.textArea.setWrapStyleWord(true);
		this.textArea.setLineWrap(true);
		this.textArea.setBackground(new Color(0, 0, 0, 0));
		this.textArea.setOpaque(false);
		this.getContentPane().add(textArea, BorderLayout.SOUTH);
		
	}
	
	public void setTitleBar(boolean show) {
		if (show) {
			this.add(this.title, BorderLayout.NORTH, 0);
		} else {
			this.remove(this.title);
		}
	}
	
}
