package e.dante.sts;

public class Potion {
    private String imgUrl;
    private String name;
    private String description;

    public Potion() {
    }

    public Potion(String imgUrl, String name, String description) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
}
