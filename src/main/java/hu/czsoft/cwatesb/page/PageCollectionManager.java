package hu.czsoft.cwatesb.page;

import hu.czsoft.data.manager.collection.InMemoryCollection;

public class PageCollectionManager extends InMemoryCollection<Page> {

    @Override @SuppressWarnings("unchecked")
    public Class<Page> getItemClass() {
        return Page.class;
    }
}
