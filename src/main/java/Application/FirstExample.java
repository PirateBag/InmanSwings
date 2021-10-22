package Application;
import java.awt.*;    
import javax.swing.*;

public class FirstExample {
	public static void run(String[] args) {
		JFrame f = new JFrame("Login Screen");// creating instance of JFrame

		f.setSize(400, 500);// 400 width and 500 height
		f.setVisible(true);// making the frame visible


		JButton b = new JButton("Login");// creating instance of JButton
  b.setBounds(130, 100, 100, 40);// x axis, y axis, width, height

		
		var username = new JTextField( "username");
		 username.setBounds( 50,150,200,30 );
		var password = new JTextField( "password");
	 password.setBounds( 50,200,200,30 );
		
		f.setLayout( new GridLayout(3,1,50,50) );

		f.add(b);// adding button in JFrame
		f.add( username );
		f.add( password );
		
			
		
	}
	public static void secondExample() {
		JFrame f;    
    
		    f=new JFrame();    
		    JButton b1=new JButton("1");    
		    JButton b2=new JButton("2");    
		    JButton b3=new JButton("3");    
		    JButton b4=new JButton("4");    
		    JButton b5=new JButton("5");    
		    JButton b6=new JButton("6");    
		    JButton b7=new JButton("7");    
		     // adding buttons to the frame       
		    f.add(b1); f.add(b2); f.add(b3);  
		    f.add(b4); f.add(b5); f.add(b6);  
		    f.add(b7);
			var username = new JTextField( "username");
			f.add( username );

			var password = new JTextField( "password");
			f.add( password );
		    // setting grid layout of 3 rows and 3 columns    
		    f.setLayout(new GridLayout(9,1));    
		    f.setSize(300,300);    
		    f.setVisible(true);    
		}    	

}
