package Buttons;

import Application.NextAction;
import Application.ScreenStateService;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static Application.ScreenTransitionType.POP;

public class PopWithoutActionButton extends JButton {
    Icon icon = new ImageIcon( "gui/icons/done_FILL0_wght400_GRAD0_opsz24.png" );


        public void actionPerformed(ActionEvent e) {
            ScreenStateService.evaluate(new NextAction(
                    "Cancel and return.", POP));
        }

    public PopWithoutActionButton() {
        super();
        this.setIcon( icon );
        this.setDefaultCapable(false);
        this.setToolTipText("Return to Previous Screen");
        this.addActionListener( this::actionPerformed );
    }
}
