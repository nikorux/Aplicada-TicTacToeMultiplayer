import java.util.ArrayList;

public class Command {
// Los comandos que se utilizan para la comunicación entre el servidor y los clientes
	public static final String NICK_TAKEN = "ntaken";
	public static final String NICK_CHANGED = "nchanged";
	public static final String INVITE_RECEIVED = "invite";
	public static final String INVITE_ACCEPTED = "accept";
	public static final String INVITE_DECLINED = "decline";
	public static final String NEW_USER = "newuser";
	public static final String WIN_INC = "wininc";
	public static final String DRAW_INC = "drawinc";
	public static final String DEF_INC = "definc";
	public static final String ENTERED_GAME = "enteredgame";
	public static final String FINISHED_GAME = "finishedgame";
	public static final String I_AM_NEW = "iamnew";
	public static final String OPP_MOVED = "moved";
	public static final String OPP_PLAYER_NO = "opp_player_no";
	public static final String GARBAGE = "garbage";
	public static final String SEND_TO_ALL = "1send_to_all23";
	public static final String QUIT = "quit";
	public static final String IM_OUT = "imout";
	public static final int PLAYER_ONE = 111;
	public static final int PLAYER_TWO = 112;
	public static final String NICK_OK = "nok";
	public static final String FORFEIT = "forfeit";

	private ArrayList<String> commands = new ArrayList<String>();

	public Command() {
		commands.add(NICK_TAKEN);
		commands.add(NICK_CHANGED);
		commands.add(INVITE_RECEIVED);
		commands.add(INVITE_ACCEPTED);
		commands.add(INVITE_DECLINED);
		commands.add(NEW_USER);
		commands.add(WIN_INC);
		commands.add(DRAW_INC);
		commands.add(DEF_INC);
		commands.add(ENTERED_GAME);
		commands.add(FINISHED_GAME);
		commands.add(I_AM_NEW);
		commands.add(OPP_MOVED);
		commands.add(OPP_PLAYER_NO);
		commands.add(GARBAGE);
		commands.add(SEND_TO_ALL);
		commands.add(QUIT);
		commands.add(IM_OUT);
		commands.add(Integer.toString(PLAYER_ONE));
		commands.add(Integer.toString(PLAYER_TWO));
		commands.add(NICK_OK);
	}

	public ArrayList<String> getCommands() {

		return commands;

	}
}
