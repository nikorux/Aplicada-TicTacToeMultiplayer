import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginGUI {
	private static JFrame frame;
	private static String nick = "empty";
	private static String hst = "empty";
	private static boolean quit = false;
	static boolean firstTime = true;
	private boolean connectNotPressed = true;

	public LoginGUI() {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 239, 213));
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		panel.add(Box.createRigidArea(new Dimension(0, 25)));

		JLabel nickLabel = new JLabel("Elija un nombre de usuario adecuado");
		nickLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(nickLabel);

		panel.add(Box.createRigidArea(new Dimension(0, 25)));

		JTextField nickname = new JTextField();
		nickname.setMaximumSize(new Dimension(Integer.MAX_VALUE, nickname.getPreferredSize().height + 5));
		panel.add(nickname);

		panel.add(Box.createRigidArea(new Dimension(0, 25)));

		JLabel hostLabel = new JLabel("Por favor, especifique el host");
		hostLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(hostLabel);

		panel.add(Box.createRigidArea(new Dimension(0, 25)));

		JTextField host = new JTextField(1);
		host.setMaximumSize(new Dimension(Integer.MAX_VALUE, host.getPreferredSize().height + 5));
		panel.add(host);

		panel.add(Box.createRigidArea(new Dimension(0, 25)));

		JButton button = new JButton("Conectar");
		button.setBackground(new Color(255, 255, 255));
		button.addActionListener(e -> {
			nick = nickname.getText();
			hst = host.getText();
			setConnectNotPressed(false);

		});
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(button);

		frame = new JFrame("Conectar al servidor");

		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(frame, "Seguro que quieres salir del panel de inicio de sesión?",
						"\"Confirma tu elección", JOptionPane.YES_NO_OPTION);

				if (confirmed == JOptionPane.YES_OPTION) {
					frame.dispose();
					Client.terminate();
					try {
						Client.close();
					} catch (NullPointerException exception) {
					}
				}
			}
		});
		frame.getContentPane().add(panel);
	}

	public boolean notConfirmed() {
		return connectNotPressed;
	}

	public void setConnectNotPressed(boolean n) {
		connectNotPressed = n;
	}

	 // Mostrar la GUI
	public void run() {

		frame.setVisible(true);

	}

 	// Bye, bye GUI
	public static void quit() {
		quit = true;
		firstTime = true;
		frame.dispose();
	}

	public static String getNickname() {
		return nick;
	}

	public static String getHost() {
		return hst;
	}

	public JFrame getFrame() {
		return frame;
	}

}
