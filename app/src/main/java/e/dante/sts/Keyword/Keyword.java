package e.dante.sts.Keyword;

import java.io.Serializable;
/*
    This the class where the data of a Keyword is stored.
 */
public class Keyword implements Serializable {
    private String name;
    private String type;
    private String description;

    public Keyword() {

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
