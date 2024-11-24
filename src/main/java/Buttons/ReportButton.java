package Buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ReportButton extends JButton  {
    ImageIcon originalIcon = new  ImageIcon( "gui/icons/summarize_24dp_5F6368_FILL0_wght400_GRAD0_opsz24.png" );
    Image scalableImageOfOriginal = originalIcon.getImage().getScaledInstance( 24, 24,  java.awt.Image.SCALE_SMOOTH );
    ImageIcon scalableIcon = new ImageIcon( scalableImageOfOriginal );
    public ReportButton() {
        super();
        this.setIcon( scalableIcon );
        this.setToolTipText("Report");
    }
    public ReportButton(String tip, ActionListener actionListener ) {
        super();
        this.setIcon( scalableIcon );
        this.setToolTipText(tip);
        this.addActionListener( actionListener );
    }
}

