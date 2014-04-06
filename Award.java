import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created with IntelliJ IDEA.
 * User: liguangxia
 * Date: 7/4/14
 * Time: 3:36 AM
 * To change this template use File | Settings | File Templates.
 */

@JsonPropertyOrder({"playerName", "amount", "playerKey", "title"})
public class Award {

    private String playerName;
    private int amount;
    private String playerKey;
    private String title;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getPlayerKey() {
        return playerKey;
    }

    public void setPlayerKey(String playerKey) {
        this.playerKey = playerKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
