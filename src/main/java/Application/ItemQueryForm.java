package Application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.springframework.http.ResponseEntity;

import com.inman.business.QueryParameterException;
import com.inman.model.Item;
import com.inman.model.User;
import com.inman.model.rest.ItemResponse;
import com.inman.model.rest.SearchItemRequest;
import com.inman.model.rest.StatusResponse;
import com.inman.model.rest.VerifyCredentialsRequest;
import com.inman.model.rest.VerifyCredentialsResponse;

public class ItemQueryForm {
	private static JPanel itemQuery;
	
	public static JPanel getItemQuery( ) {
		if ( itemQuery != null ) {
			return itemQuery;
		}
		
		JPanel itemQueryPanel = new JPanel( );
		itemQueryPanel.setLayout( new BoxLayout( itemQueryPanel, BoxLayout.Y_AXIS ) );
		itemQueryPanel.add( Utility.titleMaker( "Search Parameters" ), BorderLayout.PAGE_START );
		itemQueryPanel.add( Utility.labelMaker(" ", JLabel.TRAILING),
				BorderLayout.LINE_START  );
		
		JLabel errorMessage = Utility.createErrorMessage( JLabel.TRAILING  ) ;
		itemQueryPanel.add( errorMessage );
		
		JTextField itemId = Utility.createTextField( "Item Id" );
		itemQueryPanel.add( itemId );
		itemQueryPanel.add( Utility.labelMaker("or", JLabel.TRAILING),
				BorderLayout.LINE_START  );

		JTextField summaryId = Utility.createTextField( "Summary Id" );
		itemQueryPanel.add( summaryId );

		itemQueryPanel.add( Utility.labelMaker("or", JLabel.TRAILING),
				BorderLayout.LINE_START  );
		
		JTextField description = Utility.createTextField( "Description" );
		itemQueryPanel.add( description );

		itemQueryPanel.add( Utility.labelMaker(" ", JLabel.TRAILING),
				BorderLayout.LINE_START  );

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );

		JTextArea itemSummary = new JTextArea();
		itemQueryPanel.add( itemSummary );
		
		var button = new JButton( "Search");
		button.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					SearchItemRequest searchItemRequest = new SearchItemRequest(
						itemId.getText(), summaryId.getText(), description.getText() );

					String completeUrl = "http://localhost:8080/" + SearchItemRequest.queryUrl;
					ItemResponse responsePackage = Main.restTemplate.postForObject( completeUrl, searchItemRequest, ItemResponse.class );
					
					StringBuffer itemAsString = new StringBuffer();
					for ( Item queryResult : responsePackage.getData() ) {
						itemAsString.append( queryResult.getId() + " | " + queryResult.getSummaryId() + " | " + queryResult.getDescription() + "\n" );
					}
					itemSummary.setText( itemAsString.toString() );
					errorMessage.setText( "" );
				} catch (QueryParameterException qfe ) {
					errorMessage.setText( qfe.getMessage() );
				} catch ( Exception e1 ) {
					errorMessage.setText( e1.getMessage() );
				}
				/*
				ScreenStateService.refreshServer();
				if ( ScreenStateService.isServerConnected() ) {
					ScreenStateService.evaluate(
							new Action( "login", ScreenTransitionType.REPLACE, FormsLibrary.empty ) );
				}*/

			}
		} );  

		buttonPanel.add(button);

		buttonPanel.add( new JButton( "Exit") );
		itemQueryPanel.add( buttonPanel );
		
		FormsLibrary.itemQuery = itemQueryPanel;
		ScreenStateService.primaryPanel.add( itemQueryPanel );
		return itemQueryPanel; 
	}
}
