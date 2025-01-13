package hu.czsoft.cdn;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AtomicAsset {
    private final AssetType type;
    private String url;

    public AtomicAsset(AssetType type) {
        this.type = type;
    }

    protected AtomicAsset(AssetType type, String url) {
        this(type);
        this.url = url;
    }

    public static AtomicAsset of(AssetType type, String url) {
        return new AtomicAsset(type, url);
    }
}
