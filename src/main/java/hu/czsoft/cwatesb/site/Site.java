package hu.czsoft.cwatesb.site;

import java.util.List;

public interface Site {
    String getId();

    String getDefaultLang();

    String getShortName();

    String getFullName();

    String getPrimaryColor();

    String getSiteUrl();

    String getCdnUrl();

    CopyrightHolder getCopyrightHolder();

    List<Product> getProducts();

    List<Theme> getThemes();
}
