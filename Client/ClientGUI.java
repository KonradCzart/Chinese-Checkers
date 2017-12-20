package Client;

import javax.swing.*;
import java.awt.*;

public class ClientGUI extends JFrame
{
	private JButton startGameButton;
	private JButton loginButton;
	private JTextField playerNameTextField;
	private JLabel connectedLabel;
	private JLabel helloLabel;
	private JLabel enterNameLabel;
	private JPanel upperPanel;
	private JPanel gamePanel;
	private String playerName;

	/**
	 * Creates client GUI, starts with login panel
	 */
	public ClientGUI()
	{
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Chinese Checkers Game");
		setPreferredSize(new Dimension(640, 480));
		setMinimumSize(new Dimension(640, 480));
		setLayout(new GridBagLayout());

		loginLayer();

		add(upperPanel, new GridBagConstraints());
		pack();
		setVisible(true);
	}

	/**
	 * Creates panel to user login
	 */
	private void loginLayer()
	{
		enterNameLabel = new JLabel("Enter your name: ");
		loginButton = new JButton("Login");
		playerNameTextField = new JTextField(15);
		upperPanel = new JPanel(new GridBagLayout());

		enterNameLabel.setFont(new Font("Lucida Sans", Font.BOLD, 24));
		enterNameLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		playerNameTextField.setSize(30,30);
		loginButton.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(upperPanel.getBackground(), 20),
				BorderFactory.createEmptyBorder(10, 20, 10, 20)));
		loginButton.setFocusPainted(false);
		loginButton.setFont(new Font("Lucida Sans", Font.BOLD, 14));

		upperPanel.add(enterNameLabel);
		upperPanel.add(playerNameTextField);
		upperPanel.add(loginButton);
		getRootPane().setDefaultButton(loginButton);

		performLoginButton();
	}

	/**
	 * Creates window view for the player after login
	 */
	private void prepareAnteroomLayer()
	{
		loginButton.setEnabled(false);
		remove(upperPanel);
		gamePanel = new JPanel();
		gamePanel.setBackground(Color.cyan);
		upperPanel = new JPanel();
		upperPanel.setBackground(Color.white);
		setLayout(new BorderLayout());

		connectedLabel = new JLabel("You are connected with server! "); //TODO boolean
		connectedLabel.setForeground(Color.green);
		helloLabel = new JLabel("Welcome " + playerName);
		startGameButton = new JButton("Start game!");
		startGameButton.setFocusPainted(false);
		getRootPane().setDefaultButton(startGameButton);

		upperPanel.add(helloLabel);
		upperPanel.add(connectedLabel);
		upperPanel.add(startGameButton);
		add(upperPanel, BorderLayout.NORTH);
		add(gamePanel);
		repaint();
		setVisible(true);
	}

	/**
	 * Performs login button and validates player name.
	 */
	private void performLoginButton()
	{
		loginButton.addActionListener(event ->
		{
			String name = playerNameTextField.getText();

			if(name.matches("[\\w]+"))
			{
				playerName = name;
				prepareAnteroomLayer();
			}
			else
				JOptionPane.showMessageDialog(this,
						"Your name is invalid. Use only alphanumeric characters",
						"Error",
						JOptionPane.WARNING_MESSAGE);

		});
	}
}
