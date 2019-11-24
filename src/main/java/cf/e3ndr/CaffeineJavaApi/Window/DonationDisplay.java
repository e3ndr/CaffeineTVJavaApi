package cf.e3ndr.CaffeineJavaApi.Window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import cf.e3ndr.CaffeineJavaApi.api.Util;
import cf.e3ndr.CaffeineJavaApi.api.Chat.Chat;

@SuppressWarnings("serial")
public class DonationDisplay extends JInternalFrame {
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
		StyleConstants.setBold(this.style, true);
		
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
