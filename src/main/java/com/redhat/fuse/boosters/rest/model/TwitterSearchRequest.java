package  com.redhat.fuse.boosters.rest.model;

import java.io.Serializable;

public class TwitterSearchRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int delay;
    private String term;

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String toString(){
        return "delay: "+this.delay+", search_term: "+this.term;
    }
}