package e.dante.sts;

import java.util.List;

public class Globals {


    private static Globals instance = new Globals();

    // Getter-Setters
    public static Globals getInstance() {
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    private List<String> cards;
    private List<String> relics;
    private List<String> keywords;
    private List<String> events;
    private List<String> potions;


    private Globals() {

    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public List<String> getRelics() {
        return relics;
    }

    public void setRelics(List<String> relics) {
        this.relics = relics;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public List<String> getPotions() {
        return potions;
    }

    public void setPotions(List<String> potions) {
        this.potions = potions;
    }
}
