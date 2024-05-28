package hu.czsoft.cwatesb.persistent;

import hu.czsoft.cwatesb.model.CopyrightHolder;
import hu.czsoft.cwatesb.model.Product;
import hu.czsoft.cwatesb.model.Theme;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Example data, will be removed in the next stable release
 */
@Getter @Setter @Deprecated
public final class SiteConfig {
    private String id;

    private String name;

    private String shortName;

    private String defaultLang;

    private String themeColor;

    private String cdnUrl;

    private CopyrightHolder copyrightHolder;

    private List<Product> products = new ArrayList<>();

    private List<Theme> themeStore = new ArrayList<>();

    public SiteConfig(String id, String name, String shortName, String defaultLang, String themeColor, String cdnUrl, CopyrightHolder copyrightHolder) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.defaultLang = defaultLang;
        this.themeColor = themeColor;
        this.cdnUrl = cdnUrl;
        this.copyrightHolder = copyrightHolder;
    }

    public static SiteConfig getInstance() {
//        return getInstance("http://localhost:47355/");
        return getInstance("https://cdn.czsoft.dev/");
    }
    public static SiteConfig getInstance(String cdnUrl) {
        String logo = "";
        var config = new SiteConfig("czompiweb",
                ApplicationPersistentData.getName() + " v" + ApplicationPersistentData.getVersion(),
                ApplicationPersistentData.getFullName(),
                "en",
                "#0A4591",
                cdnUrl,
                new CopyrightHolder("https://czsoft.hu", "Czompi Software", logo));
        config.setThemeStore(List.of(
                new Theme("cwctma", "4.2.0"),
                new Theme("cwctma", "4.2.0", "style.czompihu.css")
        ));
        config.setProducts(List.of(
                new Product("czompiweb", "Czompi", "https://czompi.hu"),
                new Product("czsoftweb", "Czompi Software", "https://czsoft.hu")
        ));
        return config;
    }
}
