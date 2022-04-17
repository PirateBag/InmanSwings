package Forms;

import Application.*;
import Application.NextAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartServer extends InmanPanel {
    public StartServer() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Utility.titleMaker("Please Start the Inman Application Server"), BorderLayout.PAGE_START);
        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);
        add(Utility.subTitleMaker("Expecting server at " + Constants.IMAN_SERVER_REQUEST, JLabel.TRAILING),
                BorderLayout.LINE_START);
        var button = new JButton("Retry");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScreenStateService.refreshServer();
                if (ScreenStateService.isServerConnected()) {
                    ScreenStateService.evaluate(
                            new NextAction("login", ScreenTransitionType.REPLACE, Login.class));
                }

            }
        });
        add(button);


        ScreenStateService.primaryPanel.add(this);
    }

}
