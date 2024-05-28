package hu.czsoft.cwatesb.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class Product {

    private String id;

    private String name;

    private String url;

    public Product(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }
}
