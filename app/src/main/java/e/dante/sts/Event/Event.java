package e.dante.sts.Event;

/*
    This the class where the data of a Event is stored.
 */

public class Event {
    private String name;
    private String options;
    private int act;

    public Event() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public int getAct() {
        return act;
    }

    public void setAct(int act) {
        this.act = act;
    }
}
