package hu.czsoft.cdn;

import com.github.zafarkhaja.semver.Version;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
@Getter@Setter
public class Theme extends Asset {
    private String media;

    public Theme() {
        super(AssetType.CSS);
    }

    public static Theme of(String name, Version version, String fileName) {
        return new Theme(name, version, fileName, null, null);
    }

    public static Theme of(String name, Version version, String fileName, String media) {
        return new Theme(name, version, fileName, null, media);
    }

    public static Theme of(URL url, String media) {
        return new Theme(null, null, null, url.toString(), media);
    }

    private Theme(String name, Version version, String fileName, String url, String media) {
        super(AssetType.CSS, name, version, fileName, url);
        this.media = media;
    }
}
