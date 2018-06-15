package e.dante.sts.Keyword;

import java.io.Serializable;

public class Keyword implements Serializable {
    private String name;
    private String description;

    public Keyword() {

    }

    public Keyword(String name, String description) {
        this.name = name;
        this.description = description;
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
