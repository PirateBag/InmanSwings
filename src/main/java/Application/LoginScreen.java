package Application;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

public class LoginScreen {
	public static JPanel  go() {
		JPanel loginPanel = new JPanel();
		//  loginPanel.setLayout( new BoxLayout( pane, BoxLayout.PAGE_AXIS ) );
		return loginPanel;
	}

}
