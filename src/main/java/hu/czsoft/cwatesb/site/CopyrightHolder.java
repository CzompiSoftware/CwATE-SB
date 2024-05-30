package hu.czsoft.cwatesb.site;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class CopyrightHolder {
    private String url;
    private String name;
    private String logo;

    public CopyrightHolder() {

    }
    public CopyrightHolder(String url, String name, String logo) {
        this();
        this.name = name;
        this.url = url;
        this.logo = logo;
    }
}
