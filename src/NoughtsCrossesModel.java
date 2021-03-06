

/**
 * Representa un modelo de tic tac toe
 */
import java.util.Observable;

public class NoughtsCrossesModel extends Observable {
	private NoughtsCrosses oxo;

	public NoughtsCrossesModel(NoughtsCrosses oxo) {
		super();
		this.oxo = oxo;
	}

	public int get(int i, int j) {
		return oxo.get(i, j);
	}

	public boolean isOver() {
		return oxo.isOver();
	}

	/** Es el turno de la cross / cruz?
	 * @return  true si es el turno de la X, false para el turno de la O
	 */
	public boolean isCrossTurn() {
		return oxo.isCrossTurn();
	}

	public void turn(int i, int j) {
		oxo.turn(i, j);
		setChanged();
		notifyObservers();
	}

	/** Determine quién ha ganado / Fatality
	 * @return CROSS si X ha ganado, NAUGHT si O ha ganado, de lo contrario
	 *         BLANK
	 */
	public int whoWon() {
		return oxo.whoWon();
	}

	public void newGame() {
		oxo.newGame();
		setChanged();
		notifyObservers();
	}

	public void setWinner(int who) {
		oxo.setWinner(who);
		setChanged();
		notifyObservers();
	}

	public boolean wonBySurrender() {
		return oxo.wonBySurrender();
	}
}