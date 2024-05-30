package hu.czsoft.cwatesb.page.navbar;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NavbarNodeElementImpl implements NavbarNodeElement {

    private String id;
    private String name;
    private NavbarNodeElement child;

    public NavbarNodeElementImpl() {
        this(null, null, null);
    }
    public NavbarNodeElementImpl(String name) {
        this(null, name, null);
    }

    public NavbarNodeElementImpl(String id, String name, NavbarNodeElement child) {
        this.name = name;
        this.child = child;
    }
    public static NavbarNodeElementImpl of(String id, String name, NavbarNodeElement child ) {
        return new NavbarNodeElementImpl( id, name, child );
    }
    public static NavbarNodeElementImpl of(String name, NavbarNodeElement child ) {
        return new NavbarNodeElementImpl( null, name, child );
    }
    public static NavbarNodeElementImpl of(String name ) {
        return new NavbarNodeElementImpl(name);
    }
    public static NavbarNodeElementImpl of() {
        return new NavbarNodeElementImpl();
    }
}
