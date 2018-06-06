package e.dante.sts;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String name;
    private List<String> options;
    private int act;

    public Event() {
    }

    public Event(String name, List<String> options, int act) {
        this.name = name;
        this.options = options;
        this.act = act;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getAct() {
        return act;
    }

    public void setAct(int act) {
        this.act = act;
    }
}
