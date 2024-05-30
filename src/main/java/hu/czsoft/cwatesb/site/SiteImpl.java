package hu.czsoft.cwatesb.site;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
public class SiteImpl implements Site {
    private static final Logger LOGGER = LogManager.getLogger(Site.class);

    private String id;

    private String defaultLang;

    private String shortName;

    private String fullName;

    private String primaryColor;

    private String siteUrl;

    private String cdnUrl;

    private CopyrightHolder copyrightHolder = new CopyrightHolder();

    private List<Product> products = new ArrayList<>();

    private List<Theme> themes = new ArrayList<>();

    @Expose(serialize = false, deserialize = false)
    private List<String> styles = new ArrayList<>();


    public SiteImpl() {

    }

    public SiteImpl(String id, String shortName, String fullName, String defaultLang, String primaryColor, String siteUrl, String cdnUrl, CopyrightHolder copyrightHolder, List<Product> products, List<Theme> themes) {
        this();
        this.id = id;
        this.shortName = shortName;
        this.fullName = fullName;
        this.defaultLang = defaultLang;
        this.primaryColor = primaryColor;
        this.siteUrl = siteUrl;
        this.cdnUrl = cdnUrl;
        this.copyrightHolder = copyrightHolder;
        this.products = products;
        this.themes = themes;
    }

    public static SiteImpl of(Site site) {
        return (SiteImpl) site;
    }
}
