package Buttons;

import Application.NextAction;
import Application.ScreenStateService;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static Application.ScreenTransitionType.POP;

public class CancelWithoutActionButton extends JButton {
    Icon icon = ButtonUtility.basicButtonScaler( "gui/icons/close_FILL0_wght400_GRAD0_opsz24.png" );

        public void actionPerformed(ActionEvent e) {
            ScreenStateService.evaluate(new NextAction(
                    "Cancel and return.", POP));
        }

    public CancelWithoutActionButton() {
        super();
        this.setIcon( icon );
        this.setDefaultCapable(false);
        this.setToolTipText("Cancel and Return to Previous Screen");
        this.addActionListener( this::actionPerformed );
    }
}
