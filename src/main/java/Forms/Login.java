package Forms;

import Application.*;
import Buttons.LoginButton;
import com.inman.model.User;
import com.inman.model.rest.StatusResponse;
import com.inman.model.rest.VerifyCredentialsRequest;
import com.inman.model.rest.VerifyCredentialsResponse;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends InmanPanel {

    public Login() {

        setLayout(new BoxLayout( this, BoxLayout.Y_AXIS));

        add(Utility.titleMaker("Login"), BorderLayout.PAGE_START);

        JPanel innerBox = new JPanel();
        innerBox.setLayout(new BoxLayout(innerBox, BoxLayout.Y_AXIS));

        JLabel errorMessage = Utility.createErrorMessage(JLabel.TRAILING);
        innerBox.add(errorMessage);

        JTextField username = Utility.createTextField("User");
        username.setText("fred");
        innerBox.add(username);

        JTextField password = Utility.createTextField("Password");
        password.setText("dilban");
        innerBox.add(password);

        innerBox.add(Utility.labelMaker(" ", JLabel.LEFT),
                BorderLayout.LINE_START);
        add(innerBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        var button = new LoginButton(  );
        buttonPanel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                VerifyCredentialsRequest verifyCredentialsRequest =
                        new VerifyCredentialsRequest(username.getText(), password.getText());

                RestTemplate restTemplate = Utility.getRestTemplate();

                VerifyCredentialsResponse verifyCredentialsResponse = restTemplate.postForObject(
                        "http://localhost:8080/" + VerifyCredentialsRequest.rootUrl,
                        verifyCredentialsRequest, VerifyCredentialsResponse.class);

                if (verifyCredentialsResponse.getStatus().equals(StatusResponse.INMAN_OK)) {
                    User user = new User();
                    user.setUserName(username.getText());
                    /*ScreenStateService.evaluate(
                            new Action("itemQuery", ScreenTransitionType.REPLACE, FormsLibrary.getItemQuery())); */
                    ScreenStateService.evaluate(
                            new NextAction("itemQuery", ScreenTransitionType.REPLACE, ItemQuery.class ));
                } else {
                    Utility.setErrorMessage(errorMessage, verifyCredentialsResponse.getMessage());
                }
            }
        });


        innerBox.add(buttonPanel, BorderLayout.PAGE_END);

        ScreenStateService.primaryPanel.add( this );
    }
}
