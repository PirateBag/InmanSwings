package Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.awt.*;
import javax.swing.*;

@SpringBootApplication
@ComponentScan
public class Main { 
public static boolean RIGHT_TO_LEFT = false;



public static void addComponentsToPane(Container pane) {
    if (!(pane.getLayout() instanceof BorderLayout)) {
        pane.add(new JLabel("Container doesn't use BorderLayout!"));
        return;
    }
     
    if (RIGHT_TO_LEFT) {
        pane.setComponentOrientation(
                java.awt.ComponentOrientation.RIGHT_TO_LEFT);
    }
     
    JButton button = new JButton("Welcome to Inman");
    pane.add(button, BorderLayout.PAGE_START);
     
    button = new JButton("MenuBar");
    button.setPreferredSize(new Dimension(100,600));  
    pane.add( button, BorderLayout.LINE_START );

    //  pane.add( FormsLibrary.getStartServer()  );
		
    button = new JButton("Status Button" );
    button.setPreferredSize( new Dimension( 800,40 ));
    pane.add(button, BorderLayout.PAGE_END );
	}


public static void main(String[] args) {
    /* Use an appropriate Look and Feel */
    try {
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    } catch (UnsupportedLookAndFeelException ex) {
        ex.printStackTrace();
    } catch (IllegalAccessException ex) {
        ex.printStackTrace();
    } catch (InstantiationException ex) {
        ex.printStackTrace();
    } catch (ClassNotFoundException ex) {
        ex.printStackTrace();
    }
    /* Turn off metal's use bold fonts */
    UIManager.put("swing.boldMetal", Boolean.FALSE);
        
    //Schedule a job for the event dispatch thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            new Driver().go();
        }
    });
}
}
