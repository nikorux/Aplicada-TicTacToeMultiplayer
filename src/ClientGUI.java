import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

public class ClientGUI {
	private JFrame frame;
	private ArrayList<String> invitesArrayList;
	private JList<String> inviteList;
	private String[] columnNames = { "Usuario", "Victorias", "Empates", "Derrotas", "En juego" };
	private String[][] tableScoreValues;
	private TableModel model;


	public ClientGUI(String myName, String[][] tsv, Client client) {
		// configuraci�n de gr�ficos (tabla de puntuaci�n, lista de invitaciones, botones y sus
		// oyentes/listeners
		this.invitesArrayList = new ArrayList<String>();
		this.tableScoreValues = tsv;
		frame = new JFrame("Client for " + myName);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		JPanel scores = new JPanel();
		scores.setLayout(new BoxLayout(scores, BoxLayout.PAGE_AXIS));
		JLabel scoreLabel = new JLabel("Tabla de puntuaciones");
		scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		scores.add(scoreLabel);
		scores.add(Box.createRigidArea(new Dimension(0, 10)));

		model = new DefaultTableModel(tableScoreValues, columnNames) {
			public boolean isCellEditable(int row, int column) {
				return false;//  Esto hace que todas las celdas no sean editables
			}
		};

		JTable table = new JTable(model);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scores.add(new JScrollPane(table));

		JButton playAgainst = new JButton("Enviar invitaci�n");
		playAgainst.addActionListener(e -> {

			if (table.getSelectedRow() == -1)
				JOptionPane.showMessageDialog(frame, "Por favor, seleccione primero un oponente.", "No se seleccion� ning�n oponente.",
						JOptionPane.ERROR_MESSAGE);
			else if (client.getStats().get(myName).isInGame()) // si en medio del juego, le doy a enviar invitaciones
				JOptionPane.showMessageDialog(frame, "Est�s en medio de un juego. No puedes enviar invitaciones ahora.",
						"No se puede enviar invitaci�n", JOptionPane.ERROR_MESSAGE);
			else {
				String opp = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);
				if (opp.equals(myName))
					JOptionPane.showMessageDialog(frame, "No puede invitarse a si mismo.", "No se puede enviar invitaci�n.",
							JOptionPane.ERROR_MESSAGE);
				else if (!client.getStats().contains(opp))
					JOptionPane.showMessageDialog(frame,
							"Mientras tanto, el jugador se ha desconectado. Actualice y vuelva a intentarlo.",
							"No se puede enviar invitaci�n", JOptionPane.ERROR_MESSAGE);
				else if (client.getStats().get(opp).isInGame())
					JOptionPane.showMessageDialog(frame, "El jugador ya ha entrado en un juego. Por favor, actualice.",
							"No se puede enviar invitaci�n", JOptionPane.ERROR_MESSAGE);
				else if (client.getSentInvites().contains(opp))
					JOptionPane.showMessageDialog(frame, "Ya lo has invitado y a�n no ha respondido.",
							"No se puede enviar invitaci�n", JOptionPane.ERROR_MESSAGE);
				else
				{
					Client.tell(Command.INVITE_RECEIVED);
					Client.tell(opp);
					client.addToSentInvites(opp);
				}
			}
		});
		JButton scoreRefresh = new JButton("Refrescar puntuaciones");
		scoreRefresh.addActionListener(e -> {
			refreshTableScore();
		});
		JPanel scoreButtons = new JPanel();
		scoreButtons.add(playAgainst);
		scoreButtons.add(scoreRefresh);

		scores.add(scoreButtons);

		panel.add(scores);

		JPanel invites = new JPanel();
		invites.setLayout(new BoxLayout(invites, BoxLayout.PAGE_AXIS));
		JLabel inviteLabel = new JLabel("Invitaciones pendientes de");
		inviteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		invites.add(inviteLabel);
		invites.add(Box.createRigidArea(new Dimension(0, 10)));

		inviteList = new JList<String>(computeInviteArray(invitesArrayList));
		inviteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		invites.add(new JScrollPane(inviteList));

		JPanel inviteButtons = new JPanel();
		JButton accept = new JButton("Aceptar");
		accept.addActionListener(e -> {
			String opponent = inviteList.getSelectedValue();
			try {
				if (!client.getStats().contains(opponent)) {
					JOptionPane.showMessageDialog(frame, "El anfitri�n se ha desconectado. Por favor, actualice",
							"No se puede aceptar la invitaci�n", JOptionPane.ERROR_MESSAGE);
					removeInvite(opponent);
				}
else if (client.getStats().get(opponent).isInGame())
					JOptionPane.showMessageDialog(frame,
							"El anfitri�n ha entrado en otro juego mientras tanto. Actualizar.", "No se puede aceptar la invitaci�n",
							JOptionPane.ERROR_MESSAGE);
				else if (client.getStats().get(myName).isInGame())
					JOptionPane.showMessageDialog(frame, "Ya has aceptado una invitaci�n y has comenzado un juego.",
							"No se puede aceptar la invitaci�n", JOptionPane.ERROR_MESSAGE);
				else
				{
					removeInvite(opponent);
					refreshInvites();
					Client.tell(Command.ENTERED_GAME);
					Client.tell(Command.SEND_TO_ALL);
					int playerNumber = generateRandomInt(Command.PLAYER_ONE, Command.PLAYER_TWO);

					Client.tell(Command.OPP_PLAYER_NO);
					Client.tell(opponent);
					
					Client.tell(playerNumber);
					Client.tell(opponent);
					NoughtsCrossesGUI.startGame(opponent, playerNumber);
					Client.tell(Command.INVITE_ACCEPTED);
					Client.tell(opponent);
				}
			} catch (NullPointerException exc) {
				JOptionPane.showMessageDialog(frame, "Por favor, selecciona una invitaci�n primero.", "Ninguna invitaci�n seleccionada",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		JButton decline = new JButton("Rechazar");
		decline.addActionListener(e -> {
			String from = inviteList.getSelectedValue();
			try {
				if (!client.getStats().contains(from)) {
					JOptionPane.showMessageDialog(frame, "El anfitrion ha cerrado sesion. Quitando invitaci�n", "Anfitri�n OFF",
							JOptionPane.INFORMATION_MESSAGE);
					removeInvite(from);
				} else {
					removeInvite(from);
					Client.tell(Command.INVITE_DECLINED);
					Client.tell(from);
				}
				refreshInvites();
			} catch (NullPointerException ex) {
				JOptionPane.showMessageDialog(frame, "Por favor, selecciona una invitaci�n primero.", "Ninguna invitaci�n seleccionada",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		JButton inviteRefresh = new JButton("Refrescar lista");
		inviteRefresh.addActionListener(e -> refreshInvites());

		inviteButtons.add(accept);
		inviteButtons.add(decline);
		inviteButtons.add(inviteRefresh);
		invites.add(inviteButtons);

		panel.add(Box.createRigidArea(new Dimension(0, 10)));

		panel.add(invites);
		panel.add(Box.createRigidArea(new Dimension(0, 25)));

		JButton quit = new JButton("Salir");
		quit.addActionListener(e -> {
			int confirmed = JOptionPane.showConfirmDialog(frame, "�Estas seguro de que quieres salir del cliente?",
					"Confirma tu elecci�n", JOptionPane.YES_NO_OPTION);
			if (confirmed == JOptionPane.YES_OPTION) {
				client.quit();
				frame.dispose();
				Client.kill();
			}

		});
		quit.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(quit);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));

		frame.setSize(400, 540);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(frame, "�Estas seguro de que quieres salir del cliente?",
						"Confirma tu eleccion", JOptionPane.YES_NO_OPTION);
				if (confirmed == JOptionPane.YES_OPTION) {
					client.quit();
					frame.dispose();
					Client.kill();
				}
			}
		});
		frame.add(panel);

	}

//	
//	 Agregar una invitaci�n a la lista
//	 
//	 invite= la invitacion a ser agregada

	public void addInvite(String invite) {
		invitesArrayList.add(invite);
	}



	public void removeInvite(String invite) {

		

	// Eliminar una invitaci�n de la lista
		invitesArrayList.remove(invite);

	}

	
	public String[] computeInviteArray(ArrayList<String> invites) {

	//transforma el ArrayList de invitaciones en una matriz para que
	//puede ser utilizado por la JList

		String[] invArray = new String[invites.size()];
		for (int i = 0; i < invArray.length; i++) {
			invArray[i] = invites.get(invites.size() - 1 - i);
		}

		return invArray;
	}

	//Actualizaci�n de la lista de invitados (gr�ficos)
	public void refreshInvites() {
		inviteList.setListData(computeInviteArray(invitesArrayList));
	}
	
	//Para mostrar la interfaz gr�fica
	public void show() {
		frame.setVisible(true);

	}

	// Establece los valores de la tabla de puntuaci�n
	// newValues== los nuevos valores que se asignar�n

	public void setTableScoreValues(String[][] newValues) {
		tableScoreValues = newValues;
	}

	//  Para actualizar el puntaje de la tabla
	public void refreshTableScore() {
		((DefaultTableModel) model).setDataVector(tableScoreValues, columnNames);
	}

	// generando un numero int aleatorio.
	private int generateRandomInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	public JFrame getFrame() {
		return frame;
	}

}
