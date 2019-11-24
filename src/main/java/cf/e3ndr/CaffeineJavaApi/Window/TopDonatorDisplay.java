package cf.e3ndr.CaffeineJavaApi.Window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import cf.e3ndr.CaffeineJavaApi.api.Util;
import cf.e3ndr.CaffeineJavaApi.api.Chat.Chat;
import cf.e3ndr.CaffeineJavaApi.api.Chat.DigitalItem;

@SuppressWarnings("serial")
public class TopDonatorDisplay extends JInternalFrame {
	private Chat topDonator;
	JLabel avatar = new JLabel();
	JTextPane textArea = new JTextPane();
	private Style style;
	
	public TopDonatorDisplay() {
		this.setBounds(800, 700, 300, 150);
		
		this.add(this.avatar, BorderLayout.WEST);
		this.avatar.setSize(65, 65);
		this.style = this.textArea.addStyle("Top Donator", null);
		this.textArea.setEditable(false);
		this.textArea.setOpaque(false);
		this.setOpaque(false);
		this.add(this.textArea, BorderLayout.CENTER);

	}
	
	public void chat(Chat chat, Color textColor, boolean greenMode) {
		DigitalItem cht = chat.getDigitalItem();
		
		System.out.println(chat);
		
		if ((this.topDonator == null) || (this.topDonator.getDigitalItem().getCreditsPerItem() * this.topDonator.getDigitalItem().getCount()) < (cht.getCreditsPerItem() * cht.getCount())) {
			this.topDonator = chat;
			
			BufferedImage toPaint = Util.resizeImage(chat.getDigitalItem().getStaticBufferedImage(), 120, 120);
			StyledDocument doc = this.textArea.getStyledDocument();
			String username = chat.getSender().getUsername();
			Random rand = new Random();
			Color randomColor = new Color(rand.nextFloat(), greenMode ? 0 : rand.nextFloat(), rand.nextFloat());
			
			try {
				StyleConstants.setForeground(this.style, randomColor);
				StyleConstants.setFontSize(this.style, 15);
				doc.insertString(doc.getLength(), username, this.style);
				StyleConstants.setForeground(this.style, textColor);
				doc.insertString(doc.getLength(), " (", this.style);
				doc.insertString(doc.getLength(), String.valueOf(cht.getCount()), this.style);
				doc.insertString(doc.getLength(), ")", this.style);
				
			} catch (BadLocationException e) {}
			
			this.textArea.repaint();
			this.repaint();
			
			this.avatar.setIcon(new ImageIcon(toPaint));
			
			this.avatar.setIcon(new ImageIcon(toPaint));
			
		}
	}
}
