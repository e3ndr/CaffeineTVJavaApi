package cf.e3ndr.CaffeineJavaApi.Window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JInternalFrame;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import cf.e3ndr.CaffeineJavaApi.api.Chat.Chat;

@SuppressWarnings("serial")
public class ChatDisplay extends JInternalFrame {
	private BasicInternalFrameTitlePane title  = (BasicInternalFrameTitlePane) ((BasicInternalFrameUI) this.getUI()).getNorthPane();
	JTextPane textArea = new JTextPane();
	Style style;
	
	public ChatDisplay() {
		super("Chat Display");
		this.setResizable(true);
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
	
}