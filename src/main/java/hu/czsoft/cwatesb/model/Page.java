package hu.czsoft.cwatesb.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Page {

    private Metadata metadata;

    private String url;

    private String content;

    public Page(Metadata metadata, String content, String url) {
        this.metadata = metadata;
        this.url = url;
        this.content = content;
    }
}
