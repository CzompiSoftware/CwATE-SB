package hu.czsoft.cwatesb.model;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class Site {

    private String id;

    private String defaultLang;

    private String shortName;

    private String name;

    private String themeColor;

    @Setter
    private String baseUrl;

    private String cdnUrl;

    private List<Product> products;

    private List<Metadata> pages;

    private List<String> styles;


    public Site(String id, String shortName, String name, String defaultLang, String themeColor, String baseUrl, String cdnUrl, List<Product> products, List<Metadata> pages, List<String> styles) {
        this.id = id;
        this.shortName = shortName;
        this.name = name;
        this.defaultLang = defaultLang;
        this.themeColor = themeColor;
        this.baseUrl = baseUrl;
        this.cdnUrl = cdnUrl;
        this.products = products;
        this.pages = pages;
        this.styles = styles;
    }

    public Site(String id, String shortName, String name, String defaultLang, String themeColor, String cdnUrl, List<Product> products, List<Metadata> pages, List<String> styles) {
        this(id, shortName, name, defaultLang, themeColor, null, cdnUrl, products, pages, styles);
    }

    public void setBaseUrlFromRequest(HttpServletRequest request) {
        String baseUrl = request.getRequestURL().toString();
        this.baseUrl = baseUrl.replace(request.getRequestURI(), "");

    }
}
