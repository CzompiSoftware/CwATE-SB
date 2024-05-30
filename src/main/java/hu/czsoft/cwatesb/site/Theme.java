package hu.czsoft.cwatesb.site;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;

@Getter@Setter
@ToString
public class Theme {

    private String name;
    private String version;
    private String fileName;
    private String url;

    public Theme() {}

    public Theme(String name, String version) {
        this(name, version, "master.css", null);
    }
    public Theme(String name, String version, String fileName) {
        this(name, version, fileName, null);
    }

    public Theme(URL url) {
        this(null,null,null, url.toString());
    }

    private Theme(String name, String version, String fileName, String url) {
        this();
        this.name = name;
        this.version = version;
        this.fileName = fileName;
        this.url = url;
    }
}
