package e.dante.sts.Cards;

import java.io.Serializable;
import java.util.List;

public class Card implements Serializable {
    private String name;
    private String hero;
    private String description;
    private String upgradeDescription;
    private String yourNote;
    private String imgUrl;
    private String type;
    private String rarity;
    private String cost;
    private String upgradeCost;
    private List<String> yourComboCards;
    private List<String> yourComboRelics;
    private float yourScore;
    private float averageScore;

    public Card() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHero() {
        return hero;
    }

    public void setHero(String hero) {
        this.hero = hero;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYourNote() {
        return yourNote;
    }

    public void setYourNote(String yourNote) {
        this.yourNote = yourNote;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
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

    public String getUpgradeDescription() {
        return upgradeDescription;
    }

    public void setUpgradeDescription(String upgradeDescription) {
        this.upgradeDescription = upgradeDescription;
    }

    public String getUpgradeCost() {
        return upgradeCost;
    }

    public void setUpgradeCost(String upgradeCost) {
        this.upgradeCost = upgradeCost;
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
