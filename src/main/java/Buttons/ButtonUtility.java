package Buttons;

import javax.swing.*;
import java.awt.*;

public class ButtonUtility {
    public static ImageIcon basicButtonScaler( String buttonIconFileName ) {
        ImageIcon originalIcon = new ImageIcon( buttonIconFileName  );
        Image scalableImageOfOriginal = originalIcon.getImage().getScaledInstance( 24, 24,  java.awt.Image.SCALE_SMOOTH );
        return new ImageIcon( scalableImageOfOriginal );
    }
}
