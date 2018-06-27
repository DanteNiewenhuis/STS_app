package e.dante.sts;

import java.util.ArrayList;
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

    private List<String> ironcladCards;
    private List<String> silentCards;
    private List<String> defectCards;
    private List<String> anyCards;

    private List<String> ironcladRelics;
    private List<String> silentRelics;
    private List<String> defectRelics;
    private List<String> anyRelics;

    private List<String> keywords;
    private List<String> events;
    private List<String> potions;


    private Globals() {

    }

    public List<String> getCards() {
        ArrayList<String> result = new ArrayList<>(anyCards);

        result.addAll(ironcladCards);
        result.addAll(silentCards);
        result.addAll(defectCards);

        return result;
    }

    public List<String> getCards(String kind) {
        ArrayList<String> result = new ArrayList<>(anyCards);

        switch (kind) {
            case "Neutral":
            case "Any": return getCards();
            case "Ironclad":
                result.addAll(ironcladCards);
                break;
            case "Silent":
                result.addAll(silentCards);
                break;
            case "Defect":
                result.addAll(defectCards);
                break;
        }

        return result;
    }

    public List<String> getRelics() {
        ArrayList<String> result = new ArrayList<>(anyRelics);

        result.addAll(ironcladRelics);
        result.addAll(silentRelics);
        result.addAll(defectRelics);

        return result;
    }

    public List<String> getRelics(String kind) {
        ArrayList<String> result = new ArrayList<>(anyRelics);

        switch (kind) {
            case "Neutral":
            case "Any": return getRelics();
            case "Ironclad":
                result.addAll(ironcladRelics);
                break;
            case "Silent":
                result.addAll(silentRelics);
                break;
            case "Defect":
                result.addAll(defectRelics);
                break;
        }

        return result;
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

    public List<String> getIroncladCards() {
        return ironcladCards;
    }

    public void setIroncladCards(List<String> ironcladCards) {
        this.ironcladCards = ironcladCards;
    }

    public List<String> getSilentCards() {
        return silentCards;
    }

    public void setSilentCards(List<String> silentCards) {
        this.silentCards = silentCards;
    }

    public List<String> getDefectCards() {
        return defectCards;
    }

    public void setDefectCards(List<String> defectCards) {
        this.defectCards = defectCards;
    }

    public List<String> getAnyCards() {
        return anyCards;
    }

    public void setAnyCards(List<String> anyCards) {
        this.anyCards = anyCards;
    }

    public List<String> getIroncladRelics() {
        return ironcladRelics;
    }

    public void setIroncladRelics(List<String> ironcladRelics) {
        this.ironcladRelics = ironcladRelics;
    }

    public List<String> getSilentRelics() {
        return silentRelics;
    }

    public void setSilentRelics(List<String> silentRelics) {
        this.silentRelics = silentRelics;
    }

    public List<String> getDefectRelics() {
        return defectRelics;
    }

    public void setDefectRelics(List<String> defectRelics) {
        this.defectRelics = defectRelics;
    }

    public List<String> getAnyRelics() {
        return anyRelics;
    }

    public void setAnyRelics(List<String> anyRelics) {
        this.anyRelics = anyRelics;
    }
}
