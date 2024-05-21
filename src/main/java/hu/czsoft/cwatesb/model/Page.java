package hu.czsoft.cwatesb.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Page {

    private final Metadata metadata;

    private final String url;

    private final String content;

    public Page(Metadata metadata, String content, String url) {
        this.metadata = metadata;
        this.url = url;
        this.content = content;
    }
}
