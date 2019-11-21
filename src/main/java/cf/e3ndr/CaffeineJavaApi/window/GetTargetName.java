package cf.e3ndr.CaffeineJavaApi.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import cf.e3ndr.CaffeineJavaApi.api.CaffeineProfile;

import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;

public class GetTargetName extends JDialog {
	private static final long serialVersionUID = -3832837730101297843L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private final Action actionExit = new exit();
	private final Action actionGo = new goButton();
	private static GetTargetName dialog;
	
	public GetTargetName() {
		dialog = this;
		
		setTitle("Insert a CaffineTV channel name");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		textField = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.WEST, textField, -344, SpringLayout.EAST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, textField, -70, SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, textField, -79, SpringLayout.EAST, contentPanel);
		contentPanel.add(textField);
		textField.setColumns(10);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("GO!");
		okButton.setAction(actionGo);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		JButton cancelButton = new JButton("Exit");
		cancelButton.setAction(actionExit);
		buttonPane.add(cancelButton);
		
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
	}

	private class exit extends AbstractAction {
		private static final long serialVersionUID = -7319257695528831442L;
		
		public exit() {
			putValue(NAME, "Exit");
			putValue(SHORT_DESCRIPTION, "Close the application.");
		}
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	private class goButton extends AbstractAction {
		private static final long serialVersionUID = 8711353513281026124L;
		
		public goButton() {
			putValue(NAME, "Go!");
			putValue(SHORT_DESCRIPTION, "Start the chat listener.");
		}
		public void actionPerformed(ActionEvent e) {
			CaffeineProfile profile = new CaffeineProfile(textField.getText());
			
			if (profile.isValid()) {
				dialog.setVisible(false);
				
				new ChatDisplay(profile);
			}
		}
	}
}
