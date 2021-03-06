public class StatisticEntry {

	private boolean isInGame;
	private int wins;
	private int draws;
	private int defeats;

	public StatisticEntry(int wins, int draws, int defeats, boolean isInGame) {
		this.wins = wins;
		this.draws = draws;
		this.defeats = defeats;
		this.setInGame(isInGame);
	}

	public int getWins() {
		return wins;
	}

	public void increaseWins() {
		wins++;
	}

	public int getDraws() {
		return draws;
	}

	public void increaseDraws() {
		draws++;
	}

	public int getDefeats() {
		return defeats;
	}

	public void increaseDefeats() {
		defeats++;
	}

	public boolean isInGame() {
		return isInGame;
	}

	public void setInGame(boolean isInGame) {
		this.isInGame = isInGame;
	}

}
