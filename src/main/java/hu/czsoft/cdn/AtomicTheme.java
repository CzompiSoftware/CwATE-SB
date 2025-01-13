package hu.czsoft.cdn;

import com.github.zafarkhaja.semver.Version;
import com.google.common.base.MoreObjects;
import hu.czsoft.cwatesb.model.CdnAPI;
import lombok.Getter;

@Getter
public class AtomicTheme extends AtomicAsset {
    private String media;

    public AtomicTheme() {
        super(AssetType.CSS);
    }

    protected AtomicTheme(String url, String media) {
        super(AssetType.CSS, url);
        this.media = media;
    }

    public static AtomicTheme of(String url) {
        return new AtomicTheme(url, null);
    }

    public static AtomicTheme of(String url, String media) {
        return new AtomicTheme(url, media);
    }

    public static AtomicTheme parse(String name, Version version, String fileName) {
        return parse(name, version, fileName, null);
    }

    public static AtomicTheme parse(String name, Version version, String fileName, String media) {
        return new AtomicTheme(CdnAPI.renderUrl("css/%s@v%s/%s".formatted(name, version, MoreObjects.firstNonNull(fileName, "master.css"))), media);
    }
}
