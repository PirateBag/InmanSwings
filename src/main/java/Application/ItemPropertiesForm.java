package Application;

import com.inman.business.QueryParameterException;
import com.inman.model.rest.ItemResponse;
import com.inman.model.rest.SearchItemRequest;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemPropertiesForm {
	private static JPanel itemProperty;
	
	public static JPanel getItemProperties( ) {
		if ( itemProperty != null ) {
			return itemProperty;
		}
		
		JPanel itemPropertyPanel = new JPanel( );
		itemPropertyPanel.setLayout( new BoxLayout( itemPropertyPanel, BoxLayout.Y_AXIS ) );
		itemPropertyPanel.add( Utility.labelMaker(" ", JLabel.TRAILING),
				BorderLayout.LINE_START  );
		itemPropertyPanel.setBorder( Utility.blackLine );
	
		itemPropertyPanel.add( Utility.titleMaker("Add Item "),
				BorderLayout.LINE_START  );
		
		itemPropertyPanel.add( Utility.labelMaker(" ", JLabel.TRAILING),
				BorderLayout.LINE_START  );

		ErrorText errorText = new ErrorText();
		errorText.clearError();
		itemPropertyPanel.add( errorText );

		itemPropertyPanel.add( Utility.labelMaker(" ", JLabel.TRAILING),
				BorderLayout.LINE_START  );

		JTextField summaryId = Utility.createTextField( "Summary Id" );
		summaryId.setInputVerifier(  new Verifiers.SummaryId() );
		itemPropertyPanel.add( summaryId );

		JTextField description = Utility.createTextField( "Description" );
		itemPropertyPanel.add( description );

		JTextField unitCost = Utility.createTextField( "Unit Cost" );
		itemPropertyPanel.add( unitCost );

		itemPropertyPanel.add( Utility.labelMaker(" ", JLabel.TRAILING),
				BorderLayout.LINE_START  );

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );

		var searchButton = new JButton( "Save");
		buttonPanel.add( searchButton);

		var exitButton = new JButton( "Cancel");
		buttonPanel.add( exitButton );
		
		itemPropertyPanel.add( buttonPanel );
		
		
		searchButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					SearchItemRequest searchItemRequest = new SearchItemRequest(
						"1", summaryId.getText(), description.getText() );

					String completeUrl = "http://localhost:8080/" + SearchItemRequest.queryUrl;
					RestTemplate restTemplate = new RestTemplate();
					ItemResponse responsePackage = restTemplate.postForObject( completeUrl, searchItemRequest, ItemResponse.class );
					
					errorText.clearError( );
				
				} catch (QueryParameterException qfe ) {
					errorText.signalError( qfe.getMessage() );
				} catch ( Exception e1 ) {
					errorText.signalError( e1.getMessage() );
				}
			}
		} );  

		FormsLibrary.itemPropertyPanel = itemPropertyPanel;
		ScreenStateService.primaryPanel.add( itemPropertyPanel );
		return itemPropertyPanel; 
	}
}
