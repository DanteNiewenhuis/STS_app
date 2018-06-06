package e.dante.sts;

import java.io.Serializable;

public class Relic implements Serializable {
    private String name;
    private String description;
    private String imgUrl;
    private String rarity;
    private String notes;

    public Relic() {
    }

    public Relic(String name, String description, String imgUrl, String rarity, String notes) {
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.rarity = rarity;
        this.notes = notes;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
