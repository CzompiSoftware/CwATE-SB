package hu.czsoft.cdn;

public enum AssetType {
    CSS("css"),
    JS("js"),
    IMG("img");
    public final String value;
    AssetType(String value) {
        this.value = value;
    }
}
