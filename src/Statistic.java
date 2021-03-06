import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Statistic {

	private ConcurrentMap<String, StatisticEntry> statisticTable;

	public Statistic() {
		this.statisticTable = new ConcurrentHashMap<String, StatisticEntry>();
	}

	public boolean contains(String nick) {
		return statisticTable.containsKey(nick);
	}

	public void addEntry(String nickname) {
		statisticTable.put(nickname, new StatisticEntry(0, 0, 0, false));
	}

	public void setInGame(String nickname, boolean val) {
		statisticTable.get(nickname).setInGame(val);
	}

	public void increaseWins(String nickname) {
		statisticTable.get(nickname).increaseWins();
	}


	public void increaseDraws(String nickname) {
		statisticTable.get(nickname).increaseDraws();
	}

	public void increaseDefeats(String nickname) {
		statisticTable.get(nickname).increaseDefeats();
	}

	public StatisticEntry get(String nickname) {
		return statisticTable.get(nickname);
	}

	public int size() {
		return statisticTable.size();
	}

	public ConcurrentMap<String, StatisticEntry> getTable() {
		return statisticTable;
	}

	public void removeEntry(String nickname) {
		statisticTable.remove(nickname);

	}

}
