package cf.e3ndr.CaffeineJavaApi.Window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import cf.e3ndr.CaffeineJavaApi.api.CaffeineProfile;
import cf.e3ndr.CaffeineJavaApi.api.CaffeineStream;
import cf.e3ndr.CaffeineJavaApi.api.Chat.Chat;
import cf.e3ndr.CaffeineJavaApi.api.Chat.ChatType;
import cf.e3ndr.CaffeineJavaApi.api.Listener.ChatListener;

public class Overlay extends ChatListener implements NativeKeyListener {
	// Static instance, for better handling of the overlay
	private static Overlay instance;
	
	// Everything is public to allow the settings to be set and read
	
	// The "windows"
	JFrame frame = new JFrame("CaffeineTV Java Overlay");
	ChatDisplay chatDisplay = new ChatDisplay();
	JInternalFrame toolBar = new JInternalFrame("CaffeineTV Java Overlay v8");
	JDesktopPane desktopPane = new JDesktopPane();
	DonationDisplay donationDisplay = new DonationDisplay();
	// TopDonatorDisplay topDonatorDisplay = new TopDonatorDisplay();
	JCheckBox checkChatDisplay = new JCheckBox("Chat Display");
	JCheckBox checkDonationDisplay = new JCheckBox("Donation Display");
	JCheckBox checkGreenMode = new JCheckBox("OBS mode");
	JTextField caffeineStreamSelector = new JTextField();
	
	// Everything else
	private OverlaySettings settings;
	private CaffeineStream stream;
	boolean visible = true;
	boolean blackText = true;
	Position greenModePosition;
	
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.WARNING);
		logger.setUseParentHandlers(false);
		
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException e) {
			new ErrorDialog(e.getLocalizedMessage());
			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(new Overlay());
		
		
		/*(new Thread () {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
				
				instance.onEvent("{\"publisher\":{\"abilities\":{\"broadcast\":true,\"follow\":true,\"react\":true,\"upload_avatar\":true},\"avatar_image_path\":\"/avatars/CAID89779184B82E44EF941DC5478B3FBAE3/f9b211807f81e33e-rTJbDski.png\",\"bio\":\"i like pancakes\\n\\nhttps://medal.tv/users/2311199\",\"broadcast_id\":\"mu5cqucax\",\"caid\":\"CAID89779184B82E44EF941DC5478B3FBAE3\",\"followers_count\":5,\"following_count\":7,\"is_caster\":false,\"is_featured\":false,\"is_online\":true,\"is_verified\":false,\"last_broadcasted_date\":\"2019-11-19T03:01:35.000Z\",\"name\":\"Ender\",\"stage_id\":\"89779184B82E44EF941DC5478B3FBAE3\",\"username\":\"ItzLcyx\"},\"id\":\"eyJzIjoiNkMyQTM0OUIyRDJCNDJCRjhBQzVFNjVCNUQ0RDQ5NTciLCJ1IjoiYzMyNGJlZTYtOGRmMS00NjkzLTY5MTUtMjA4Y2M5ZmYzMzI3In0=\",\"type\":\"digital_item\",\"body\":{\"text\":\"take this\",\"media\":\"\",\"digital_item\":{\"id\":\"c0d4d23e-c42a-431e-88dd-925438268eec\",\"count\":1,\"credits_per_item\":9,\"web_asset_path\":\"/digital-items/thumbsup.0afdac32ff879dae5aa33a75dbbe4368.json\",\"static_image_path\":\"/digital-items/thumbsup.513155120cddd372637b44ede74a03d3.png\",\"preview_image_path\":\"/digital-items/thumbs_preview.72338e9d831d22a9db04005b530c5d53.png\",\"scene_kit_path\":\"/digital-items/thumbsup.fdf635a7f0c3e91ef2d94892231a22eb.zip\"}}}");
				
			}
		}).start();*/ // Test image event
	}

	public Overlay() {
		instance = this;
		this.settings = new OverlaySettings(instance);
		this.initialize();
		this.frame.setVisible(true);
		this.updateSettings(false);
	}
	
	private void initialize() {
		this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.frame.setFocusable(false);
		this.frame.setAlwaysOnTop(true);
		
		this.frame.addWindowListener(new WindowListener() {
			@Override
			public void windowClosing(WindowEvent e) {
				settings.exit(instance);
				System.exit(0);
			}
			
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
		});
		
		this.desktopPane.setOpaque(false);
		this.desktopPane.setBackground(Color.GREEN);
		this.frame.getContentPane().add(this.desktopPane, BorderLayout.CENTER);
		
		this.initDisplays(desktopPane);
		this.initToolBar(desktopPane);
		
	}
	
	private void toggleDisplay() {
		this.visible = !this.visible;
		
		this.toolBar.setVisible(this.visible);
		this.chatDisplay.setTitleBar(this.visible);
		this.chatDisplay.repaint();
		this.donationDisplay.setTitleBar(this.visible);
		this.donationDisplay.repaint();
		
	}
	
	private void updateSettings(boolean update) {
		if (this.checkGreenMode.isSelected()) {
			this.frame.dispose();
			this.frame.setBackground(Color.GREEN);
			this.frame.setUndecorated(false);
			this.frame.pack();
			this.frame.setVisible(true);
			this.frame.setBounds(this.greenModePosition.x, this.greenModePosition.y, this.greenModePosition.w, this.greenModePosition.h);
			this.desktopPane.setOpaque(true);
		} else {
			if (update) this.greenModePosition = new Position(this.frame);
			
			int width = 0;
			int height = 0;
			int x = 0;
			int y = 0;
			
			for (GraphicsDevice screen : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
				DisplayMode mode = screen.getDisplayMode();
				Rectangle config = screen.getDefaultConfiguration().getBounds();
				
				width += mode.getWidth();
				height += mode.getHeight();
				
				if (config.x < x) x -= config.x;
				if (config.y < y) y += config.y;
			}
			
			this.frame.dispose();
			this.frame.setUndecorated(true);
			this.frame.setBackground(new Color(0, 0, 0, 0));
			this.frame.setVisible(true);
			this.frame.setBounds(x, y, width, height);
			this.desktopPane.setOpaque(false);
		}
		
		this.toolBar.setBounds(100, (this.frame.getHeight() / 2) - 25, 1000, 50);
		this.frame.setAlwaysOnTop(!this.checkGreenMode.isSelected());
	}
	
	private void initToolBar(JDesktopPane pane) {
		this.toolBar.setBounds(200, 600, 1000, 50);
		this.toolBar.setFrameIcon(null);
		this.toolBar.setResizable(true);
		this.toolBar.setOpaque(false);
		this.toolBar.getContentPane().setLayout(new GridLayout(0, 8, 0, 0));
		
		JCheckBox darkText = new JCheckBox("Dark text");
		darkText.setSelected(this.blackText);
		darkText.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				blackText = darkText.isSelected();
			}
		});
		
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
		
		JButton btnExit = new JButton("Exit Overlay");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				settings.exit(instance);
				System.exit(0);
			}
		});
		
		this.checkChatDisplay.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				chatDisplay.setVisible(checkChatDisplay.isSelected());
			}
		});
		
		this.checkDonationDisplay.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				donationDisplay.setVisible(checkDonationDisplay.isSelected());
			}
		});
		
		this.checkGreenMode.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				updateSettings(true);
			}
		});
		
		JButton btnCloseUI = new JButton("Hide UI");
		btnCloseUI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleDisplay();
			}
		});
		
		this.toolBar.getContentPane().add(btnCloseUI);
		this.toolBar.getContentPane().add(checkChatDisplay);
		this.toolBar.getContentPane().add(checkDonationDisplay);
		this.toolBar.getContentPane().add(darkText);
		this.toolBar.getContentPane().add(caffeineStreamSelector);
		this.toolBar.getContentPane().add(btnChangeStream);
		this.toolBar.getContentPane().add(checkGreenMode);
		this.toolBar.getContentPane().add(btnExit);
		
		this.toolBar.setVisible(true);
		pane.add(this.toolBar);
	}
	
	private void initDisplays(JDesktopPane pane) {
		pane.add(this.chatDisplay);
		this.chatDisplay.setOpaque(false);
		this.chatDisplay.setVisible(this.checkChatDisplay.isSelected());
		
		pane.add(this.donationDisplay);
		this.donationDisplay.setOpaque(false);
		this.donationDisplay.setVisible(this.checkDonationDisplay.isSelected());
		
		// pane.add(this.topDonatorDisplay);
		// this.topDonatorDisplay.setOpaque(false);
		// this.topDonatorDisplay.setVisible(true);
		
	}
	
	@Override
	public void onEvent(Chat chat) {
		this.frame.repaint();
		this.desktopPane.repaint();
		
		if (chat.getType() == ChatType.REACTION) {
			this.chatDisplay.chat(chat, this.blackText ? Color.BLACK : Color.WHITE, this.checkGreenMode.isSelected());
		} else {
			// this.topDonatorDisplay.chat(chat, this.textColor, this.greenMode);
			this.donationDisplay.chat(chat, this.blackText ? Color.BLACK : Color.WHITE, this.checkGreenMode.isSelected());
			
		}
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
	
}
