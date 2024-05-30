package hu.czsoft.cwatesb.page;

import lombok.Getter;
import lombok.ToString;

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

    private final String hash; //TODO: Implement hash?!

    public static Page of(Page page, String url) {
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
