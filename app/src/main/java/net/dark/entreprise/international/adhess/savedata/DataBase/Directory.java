package net.dark.entreprise.international.adhess.savedata.DataBase;

/**
 * Created by adhess on 31/05/2018.
 */

public class Directory {
    private long id;
    private String url;

    public Directory(long id, String url) {
        this.id = id;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
