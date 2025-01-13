package hu.czsoft.cdn;

import com.github.zafarkhaja.semver.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URL;
@Getter@Setter@ToString
public class Asset extends AtomicAsset {

    private String name;
    private Version version;
    private String fileName;

    public Asset(AssetType type) {
        super(type);
    }

    public static Asset of(AssetType type, String name, Version version, String fileName) {
        return new Asset(type, name, version, fileName, null);
    }

    Asset(AssetType type, String name, Version version, String fileName, String url) {
        super(type, url);
        this.name = name;
        this.version = version;
        this.fileName = fileName;
    }
}
