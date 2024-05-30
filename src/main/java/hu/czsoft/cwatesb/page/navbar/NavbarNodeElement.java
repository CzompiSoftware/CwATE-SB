package hu.czsoft.cwatesb.page.navbar;

public interface NavbarNodeElement {
    String getId();
    void setId(String id);

    String getName();
    void setName(String name);

    NavbarNodeElement getChild();
    void setChild(NavbarNodeElement child);
}
