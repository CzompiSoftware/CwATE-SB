package hu.czsoft.data.search;

public interface RouteElement {
    String getId();
    void setId(String id);

    String getName();
    void setName(String name);

    RouteElement getChild();
    void setChild(RouteElement child);
}
