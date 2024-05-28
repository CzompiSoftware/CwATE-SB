package hu.czsoft.cwatesb.model;

import hu.czsoft.cwatesb.TemplatingEngineApplication;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;

@Getter
@ToString
public class Page implements Comparable<Page> {
    @Override
    public int compareTo(Page other) {
        //if (itm.NavMenuId == -1) itm.NavMenuId = short.Parse($"{Math.Min(Globals.Pages.Max(x => x.NavMenuId) + Globals.Pages.Count, (int)short.MaxValue)}");-->
        //
        //.OrderBy(x => x.NavMenuId).ThenBy(y => y.Id))
        return this.getMetadata().getNavMenuId() - other.getMetadata().getNavMenuId();
    }

    private final Metadata metadata;

    private final String content;

    private String hash;

    public static Page parse(Page page, String url) {
        if (page.getMetadata().getUrl() == null) {
            page.getMetadata().setUrl(url + "/index.xmd");
        } else {
            page.getMetadata().setUrl(url + page.getMetadata().getUrl());
        }
        return page;
    }

    public Page(Metadata metadata, String content, String hash) {
        this.metadata = metadata;
        this.content = content;
        this.hash = hash;
    }
}
