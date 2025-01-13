package hu.czsoft.xmdl;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Page implements Comparable<Page> {

    @Override
    public int compareTo(Page other) {
        //if (itm.NavMenuId == -1) itm.NavMenuId = short.Parse($"{Math.Min(Globals.Pages.Max(x => x.NavMenuId) + Globals.Pages.Count, (int)short.MaxValue)}");-->
        //
        //.OrderBy(x => x.NavMenuId).ThenBy(y => y.Id))
        /*
            <!-- @foreach (Metadata page in Globals.Pages.Select(itm =>-->
            <!-- {-->
            <!--    if (itm.NavMenuId == -1) itm.NavMenuId = short.Parse($"{Math.Min(Globals.Pages.Max(x => x.NavMenuId) + Globals.Pages.Count, (int)short.MaxValue)}");-->
            <!--    return itm;-->
            <!-- }).Where(x => x.IsNavMenuItem == true).OrderBy(x => x.NavMenuId).ThenBy(y => y.Id))-->
            <!-- {-->
         */
        if(this.getMetadata().getNavbar() == null || other.getMetadata().getNavbar() == null) return 0;
        return this.getMetadata().getNavbar().getIndex() - other.getMetadata().getNavbar().getIndex();
    }

    private final Metadata metadata;

    private final String content;

    private final String hash;

    private Page() {
        this(new Metadata(), "", null);
    }
    private Page(Metadata metadata, String content, String hash) {
        this.metadata = metadata;
        this.content = content;
        this.hash = hash;
    }

    public static Page of() {
        return new Page();
    }

    public static Page of(final Metadata metadata,final String content, final String hash) {
        return new Page(metadata, content, hash);
    }

}
