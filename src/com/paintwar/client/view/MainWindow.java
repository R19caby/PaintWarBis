package com.paintwar.client.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.paintwar.client.view.pages.AccountCreation;
import com.paintwar.client.view.pages.Collection;
import com.paintwar.client.view.pages.ConnexionChoice;
import com.paintwar.client.view.pages.Home;
import com.paintwar.client.view.pages.LogIn;
import com.paintwar.client.view.pages.PageName;
import com.paintwar.client.view.pages.Parameters;
import com.paintwar.client.view.pages.SetEnd;
import com.paintwar.client.view.pages.Shop;


public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = -1263567319246462529L;
	private ConnexionChoice connexionChoicePage;
	private LogIn logInPage;
	private AccountCreation accountCreationPage;
	private Home homePage;
	private Parameters parametersPage;
	private Collection collectionPage;
	private SetEnd setEndPage;
	private Shop shopPage;
	
	private JPanel contentContainer;
	private String playerName;
	
	public void paintComponent(Graphics g) {
        try {
        	super.paintComponents(g);
        	
        	File imageFile = new File("graphicResources.paint.png");
        	Image img = ImageIO.read(imageFile);
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            this.repaint();
        } catch (Exception e ) {
        }
    }
	
	public MainWindow() {
		super();
		addWindowListener(new MyWindowListener());
		setLayout(new BorderLayout());
		setVisible(true);
		
		contentContainer = new JPanel();
		add(contentContainer, BorderLayout.CENTER);
		contentContainer.setLayout(new CardLayout());
		
		connexionChoicePage = new ConnexionChoice(this);
		logInPage = new LogIn(this);
		accountCreationPage = new AccountCreation(this);
		parametersPage = new Parameters(this);
		contentContainer.add(PageName.CONNEXION_CHOICE.toString(), connexionChoicePage);
		contentContainer.add(PageName.LOG_IN.toString(), logInPage);
		contentContainer.add(PageName.SIGN_IN.toString(), accountCreationPage);
		contentContainer.add(PageName.PARAMETERS.toString(), parametersPage);
		
		
	}
	
	public JPanel getContentContainer() {
		return contentContainer;
	}
	
	
	public class MyWindowListener implements WindowListener {
		public void windowActivated(WindowEvent arg0) {
		}
		public void windowClosed(WindowEvent arg0) {
		}
		@Override
		public void windowClosing(WindowEvent arg0) {
			System.exit(0);
		}
		public void windowDeactivated(WindowEvent arg0) {
		}
		public void windowDeiconified(WindowEvent arg0) {
		}
		public void windowIconified(WindowEvent arg0) {
		}
		public void windowOpened(WindowEvent arg0) {
		}
	}
	
	public static void main(String[] args) {
		MainWindow window = new MainWindow();
		window.setSize(new Dimension(1800, 900));
	}
	
}
