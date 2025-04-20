package Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;

public class InmanPanelV2 extends InmanPanel {
    protected JLabel title = Utility.titleMaker("Title Unassigned");
    protected JPanel buttonPanel = new JPanel();
    protected Logger logger = LoggerFactory.getLogger("controller: " + this.getClass().getName() );
    protected RestTemplate restTemplate = new RestTemplate();
    public InmanPanelV2() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);
        setBorder(Utility.blackLine);

        add(title, BorderLayout.LINE_START);

        add(Utility.labelMaker(" ", JLabel.TRAILING),
                BorderLayout.LINE_START);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    }

    public void updateStateWhenOpeningNewChild(NextAction xAction ) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    public void updateStateWhenChildCloses(NextAction xAction ) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
