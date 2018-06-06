package e.dante.sts;

import java.io.Serializable;

public class Card implements Serializable {
    private String name;
    private String color;
    private String description;
    private String notes;
    private String imgUrl;
    private String type;
    private String rarity;
    private String cost;

    public Card() {
        this.notes = "";
    }

    public Card(String name, String color, String description, String notes, String imgUrl, String type, String rarity, String cost) {
        this.name = name;
        this.color = color;
        this.description = description;
        this.notes = notes;
        this.imgUrl = imgUrl;
        this.type = type;
        this.rarity = rarity;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
}
