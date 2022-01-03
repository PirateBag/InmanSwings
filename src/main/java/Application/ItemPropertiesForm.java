package Application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.inman.business.QueryParameterException;
import com.inman.model.rest.ItemResponse;
import com.inman.model.rest.SearchItemRequest;

import Verifiers.SummaryId;

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
		
		JLabel errorMessage = Utility.createErrorMessage( JLabel.TRAILING  ) ;
		itemPropertyPanel.add( errorMessage );
		
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
					ItemResponse responsePackage = Main.restTemplate.postForObject( completeUrl, searchItemRequest, ItemResponse.class );
					
					errorMessage.setText( "" );
					
					var numRows = responsePackage.getData().length;
					
				} catch (QueryParameterException qfe ) {
					errorMessage.setText( qfe.getMessage() );
				} catch ( Exception e1 ) {
					errorMessage.setText( e1.getMessage() );
				}
			}
		} );  

		FormsLibrary.itemPropertyPanel = itemPropertyPanel;
		ScreenStateService.primaryPanel.add( itemPropertyPanel );
		return itemPropertyPanel; 
	}
}
