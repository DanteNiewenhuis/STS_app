package e.dante.sts.Relics;

import java.io.Serializable;
import java.util.List;

public class Relic implements Serializable {
    private String name;
    private String description;
    private String imgUrl;
    private String rarity;
    private String info;
    private String hero;
    private String yourNote;
    private float yourScore;
    private float averageScore;
    private List<String> yourComboCards;
    private List<String> yourComboRelics;

    public Relic() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public float getYourScore() {
        return yourScore;
    }

    public void setYourScore(float yourScore) {
        this.yourScore = yourScore;
    }

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
        this.averageScore = averageScore;
    }

    public String getHero() {
        return hero;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }

    public String getYourNote() {
        return yourNote;
    }

    public void setYourNote(String yourNote) {
        this.yourNote = yourNote;
    }

    public List<String> getYourComboCards() {
        return yourComboCards;
    }

    public void setYourComboCards(List<String> yourComboCards) {
        this.yourComboCards = yourComboCards;
    }

    public List<String> getYourComboRelics() {
        return yourComboRelics;
    }

    public void setYourComboRelics(List<String> yourComboRelics) {
        this.yourComboRelics = yourComboRelics;
    }
}
