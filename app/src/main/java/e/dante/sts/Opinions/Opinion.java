package e.dante.sts.Opinions;

import java.util.ArrayList;

/*
    A class to save all opinions from one user about a given card/relic
 */

public class Opinion {
    private String userName;
    private ArrayList<String> comboCards;
    private ArrayList<String> comboRelics;
    private ArrayList<String> antiComboCards;
    private ArrayList<String> antiComboRelics;
    private String note;
    private String score;

    // Constructor
    public Opinion() { }

    // getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public ArrayList<String> getComboRelics() {
        return comboRelics;
    }

    public void setComboRelics(ArrayList<String> comboRelics) {
        this.comboRelics = comboRelics;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<String> getAntiComboCards() {
        return antiComboCards;
    }

    public void setAntiComboCards(ArrayList<String> antiComboCards) {
        this.antiComboCards = antiComboCards;
    }

    public ArrayList<String> getAntiComboRelics() {
        return antiComboRelics;
    }

    public void setAntiComboRelics(ArrayList<String> antiComboRelics) {
        this.antiComboRelics = antiComboRelics;
    }
}
