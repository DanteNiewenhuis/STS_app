package e.dante.sts;

import java.io.Serializable;

public class Relic implements Serializable {
    private String name;
    private String description;
    private String imgUrl;
    private String Rarity;

    public Relic() {
    }

    public Relic(String name, String description, String imgUrl, String rarity) {
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        Rarity = rarity;
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
        return Rarity;
    }

    public void setRarity(String rarity) {
        Rarity = rarity;
    }
}
