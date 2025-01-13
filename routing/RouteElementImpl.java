package hu.czsoft.data.search;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RouteElementImpl implements RouteElement {

    private String id;
    private String name;
    private RouteElement child;

    public RouteElementImpl() {
        this(null, null, null);
    }
    public RouteElementImpl(String name) {
        this(null, name, null);
    }

    public RouteElementImpl(String id, String name, RouteElement child) {
        this.name = name;
        this.child = child;
    }
    public static RouteElementImpl of( String id, String name, RouteElement child ) {
        return new RouteElementImpl( id, name, child );
    }
    public static RouteElementImpl of( String name, RouteElement child ) {
        return new RouteElementImpl( null, name, child );
    }
    public static RouteElementImpl of( String name ) {
        return new RouteElementImpl(name);
    }
    public static RouteElementImpl of() {
        return new RouteElementImpl();
    }
}
