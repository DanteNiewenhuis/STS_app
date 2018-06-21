package e.dante.sts.Combos;

import java.util.ArrayList;

public class Combo {
    private String userId;
    private ArrayList<String> comboCards;
    private String score;

    public Combo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getComboCards() {
        return comboCards;
    }

    public void setComboCards(ArrayList<String> comboCards) {
        this.comboCards = comboCards;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
