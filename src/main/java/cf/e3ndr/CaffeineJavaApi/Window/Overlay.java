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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import cf.e3ndr.CaffeineJavaApi.api.CaffeineProfile;
import cf.e3ndr.CaffeineJavaApi.api.CaffeineStream;
import cf.e3ndr.CaffeineJavaApi.api.Util;
import cf.e3ndr.CaffeineJavaApi.api.Chat.Chat;
import cf.e3ndr.CaffeineJavaApi.api.Chat.ChatType;
import cf.e3ndr.CaffeineJavaApi.api.Listener.ChatListener;

public class Overlay extends ChatListener implements NativeKeyListener {
	private static Overlay instance;
	private JFrame frame = new JFrame("CaffeineTV Java Overlay");
	private ChatDisplay chatDisplay = new ChatDisplay();
	private JInternalFrame toolBar = new JInternalFrame();
	private boolean visible = true;
	private CaffeineStream stream;
	private Color textColor = Color.BLACK;
	private DonationDisplay donationDisplay = new DonationDisplay();
	private boolean greenMode = false;
	private JDesktopPane desktopPane = new JDesktopPane();
	
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
		this.initialize();
		this.frame.setVisible(true);
	}
	
	private void initialize() {
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setFocusable(false);
		this.frame.setAlwaysOnTop(true);
		this.updateSettings();
		
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
	
	private void updateSettings() {
		if (this.greenMode) {
			this.frame.dispose();
			this.frame.setBackground(Color.GREEN);
			this.frame.setUndecorated(false);
			this.frame.pack();
			this.frame.setVisible(true);
			this.frame.setBounds(100, 100, 1280, 720);
			this.chatDisplay.setLocation(0, 0);
			this.donationDisplay.setLocation(0, 0);
			this.desktopPane.setOpaque(true);
			
		} else {
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
		
		this.frame.setAlwaysOnTop(!this.greenMode);
	}
	
	private void initToolBar(JDesktopPane pane) {
		this.toolBar.setTitle("CaffeineTV Java Overlay v6");
		this.toolBar.setBounds(200, 600, 1000, 50);
		this.toolBar.setFrameIcon(null);
		this.toolBar.setResizable(true);
		this.toolBar.setOpaque(false);
		this.toolBar.getContentPane().setLayout(new GridLayout(0, 8, 0, 0));
		
		JTextField caffeineStreamSelector = new JTextField();
		caffeineStreamSelector.setText("Slatsss");
		
		JCheckBox darkText = new JCheckBox("Dark text");
		darkText.setSelected(true);
		darkText.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				textColor = darkText.isSelected() ? Color.BLACK : Color.WHITE;
				darkText.setText(darkText.isSelected() ? "Dark text" : "Light Text");
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
		
		JCheckBox checkChatDisplay = new JCheckBox("Chat Display");
		checkChatDisplay.setSelected(true);
		checkChatDisplay.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				chatDisplay.setVisible(checkChatDisplay.isSelected());
			}
		});
		
		JCheckBox checkDonationDisplay = new JCheckBox("Donation Display");
		checkDonationDisplay.setSelected(true);
		checkDonationDisplay.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				donationDisplay.setVisible(checkDonationDisplay.isSelected());
			}
		});
		
		JCheckBox checkOBSMode = new JCheckBox("OBS mode");
		checkOBSMode.setSelected(false);
		checkOBSMode.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				greenMode = checkOBSMode.isSelected();
				updateSettings();
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
		this.toolBar.getContentPane().add(checkDonationDisplay);
		this.toolBar.getContentPane().add(darkText);
		this.toolBar.getContentPane().add(caffeineStreamSelector);
		this.toolBar.getContentPane().add(btnChangeStream);
		this.toolBar.getContentPane().add(checkOBSMode);
		
		this.toolBar.setVisible(true);
		pane.add(this.toolBar);
	}
	
	private void initDisplays(JDesktopPane pane) {
		pane.add(this.chatDisplay);
		this.chatDisplay.setOpaque(false);
		this.chatDisplay.setVisible(true);
		
		pane.add(this.donationDisplay);
		this.donationDisplay.setOpaque(false);
		this.donationDisplay.setVisible(true);
		
	}
	
	@Override
	public void onEvent(Chat chat) {
		if (chat.getType() == ChatType.REACTION) {
			this.chatDisplay.chat(chat, this.textColor, this.greenMode);
			this.frame.repaint();
			this.desktopPane.repaint();
		} else {
			this.donationDisplay.chat(chat, this.textColor, this.greenMode);
			this.frame.repaint();
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
	
} @SuppressWarnings("serial") class ChatDisplay extends JInternalFrame {
	private BasicInternalFrameTitlePane title  = (BasicInternalFrameTitlePane) ((BasicInternalFrameUI) this.getUI()).getNorthPane();
	JTextPane textArea = new JTextPane();
	Style style;
	
	public ChatDisplay() {
		super("Chat Display");
		this.setResizable(true);
		this.setBounds(500, 800, 150, 200);
		this.remove(this.title);
		this.setTitleBar(true);
		this.setFrameIcon(null);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(0, 0, 0, 0), new Color(0, 0, 0, 0)));
		this.setBackground(new Color(0, 0, 0, 0));
		this.setOpaque(false);
		
		this.style = this.textArea.addStyle("Chat", null);
		StyleConstants.setBold(this.style, true);
		this.textArea.setEditable(false);
		this.textArea.setOpaque(false);
		this.add(this.textArea, BorderLayout.SOUTH);
		
	}
	
	public void chat(Chat chat, Color textColor, boolean greenMode) {
		StyledDocument doc = this.textArea.getStyledDocument();
		String username = chat.getSender().getUsername();
		
		Random rand = new Random();
		Color randomColor = new Color(rand.nextFloat(), greenMode ? 0 : rand.nextFloat(), rand.nextFloat());
		
		try {
			StyleConstants.setForeground(this.style, randomColor);
			StyleConstants.setFontSize(this.style, 15);
			doc.insertString(doc.getLength(), username, this.style);
			StyleConstants.setFontSize(this.style, 12);
			StyleConstants.setForeground(style, textColor);
			doc.insertString(doc.getLength(), ": ", this.style);
			doc.insertString(doc.getLength(), chat.getText(), this.style);
			doc.insertString(doc.getLength(), "\n", this.style);
			
		} catch (BadLocationException e) {}
		
		this.textArea.repaint();
		this.repaint();
	}

	public void setTitleBar(boolean show) {
		if (show) {
			this.add(this.title, BorderLayout.NORTH, 0);
		} else {
			this.remove(this.title);
		}
	}
	
} @SuppressWarnings("serial") class DonationDisplay extends JInternalFrame {
	private BasicInternalFrameTitlePane title  = (BasicInternalFrameTitlePane) ((BasicInternalFrameUI) this.getUI()).getNorthPane();
	private JTextPane textArea = new JTextPane();
	private Style style;
	private JLabel lbl = new JLabel();
    
	public DonationDisplay() {
		super("Donation Display");
		this.setResizable(true);
		this.setBounds(500, 400, 200, 200);
		this.remove(this.title);
		this.setTitleBar(true);
		this.setFrameIcon(null);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(0, 0, 0, 0), new Color(0, 0, 0, 0)));
		this.setBackground(new Color(0, 0, 0, 0));
		this.setOpaque(false);
		
		this.style = this.textArea.addStyle("Donation", null);
		this.textArea.setEditable(false);
		this.textArea.setOpaque(false);
		
		this.lbl.setOpaque(false);
		
		this.add(this.lbl, BorderLayout.CENTER);
		this.add(this.textArea, BorderLayout.SOUTH);
		
	}
	
	public void chat(Chat chat, Color textColor, boolean greenMode) {
		BufferedImage toPaint = Util.resizeImage(chat.getDigitalItem().getStaticBufferedImage(), lbl.getWidth(), lbl.getHeight());
		StyledDocument doc = this.textArea.getStyledDocument();
		String username = chat.getSender().getUsername();
		Random rand = new Random();
		Color randomColor = new Color(rand.nextFloat(), greenMode ? 0 : rand.nextFloat(), rand.nextFloat());
		
		try {
			StyleConstants.setForeground(this.style, randomColor);
			StyleConstants.setFontSize(this.style, 15);
			doc.insertString(doc.getLength(), username, this.style);
			StyleConstants.setFontSize(this.style, 12);
			StyleConstants.setForeground(style, textColor);
			doc.insertString(doc.getLength(), ": ", this.style);
			doc.insertString(doc.getLength(), chat.getText(), this.style);
		} catch (BadLocationException e) {}
		
		this.textArea.repaint();
		this.repaint();
		
		this.lbl.setIcon(new ImageIcon(toPaint));
		this.textArea.setText(chat.getSender().getUsername());
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {}
		
		this.textArea.setText("");
		this.lbl.setIcon(null);
		this.repaint();
	}

	public void setTitleBar(boolean show) {
		if (show) {
			this.add(this.title, BorderLayout.NORTH, 0);
		} else {
			this.remove(this.title);
		}
	}
	
}
