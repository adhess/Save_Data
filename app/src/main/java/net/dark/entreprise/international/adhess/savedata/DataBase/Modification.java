package net.dark.entreprise.international.adhess.savedata.DataBase;

/**
 * Created by adhess on 31/05/2018.
 */

public class Modification {
    private long id;
    private String url;
    private String last_modification;

    public Modification(long id, String url, String last_modification) {
        this.id = id;
        this.url = url;
        this.last_modification = last_modification;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getLast_modification() {
        return last_modification;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLast_modification(String last_modification) {
        this.last_modification = last_modification;
    }
}
