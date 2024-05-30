package hu.czsoft.cwatesb.site;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import hu.czsoft.cwatesb.TemplatingEngineApplication;
import hu.czsoft.cwatesb.engine.EngineManager;
import hu.czsoft.cwatesb.model.CdnAPI;
import hu.czsoft.data.manager.singleton.StoredSingleton;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class SiteManager extends StoredSingleton<Site> {
    private static final Logger LOGGER = LogManager.getLogger(SiteManager.class);
    private final Path fileName = Path.of(TemplatingEngineApplication.WORKING_DIRECTORY + "site.json");
    private final EngineManager engineManager;
    @Setter private List<String> styles = new ArrayList<>();

    public SiteManager(EngineManager engineManager) {
        super(TemplatingEngineApplication.WORKING_DIRECTORY + "site.json");
        this.engineManager = engineManager;
    }

    /**
     * @param id Site id (used for selecting the current product from <code>products</code>
     * @param shortName Short fullName of the site. It is displayed on the title.
     * @param name Full fullName of the site. This is the default metadata for the site.
     * @param defaultLang Default site language.
     * @param themeColor Site theme color for mobile browser header.
     * @param baseUrl Site base url
     * @param cdnUrl CDN url
     * @param copyrightHolder Copyright holder for current site
     * @param products List of public products associated to this site.
     * @param themes List of themes used in the site
     */
    public void add(String id, String shortName, String name, String defaultLang, String themeColor, String baseUrl, String cdnUrl, CopyrightHolder copyrightHolder, List<Product> products, List<Theme> themes) {
        add(new SiteImpl(id, shortName, name, defaultLang, themeColor, baseUrl, cdnUrl, copyrightHolder, products, themes));
    }

    private void setSiteUrl(String baseUrl) {
        if (get().getSiteUrl() == null) {
            SiteImpl.of(get()).setSiteUrl(baseUrl);
        }
    }

    private List<Theme> defaultThemes() {
        List<Theme> themes = new ArrayList<>();
        themes.add(new Theme("cwate", engineManager.get().getVersion().toString()));
        themes.add(new Theme("cwate", engineManager.get().getVersion().toString(), "style.%s.css".formatted(this.get().getId())));
        return themes;
    }

    @Override
    public void load() throws IOException {
        super.load();
        processStyles();
    }

    private void processStyles() {
        styles.clear();
        var themes = defaultThemes();
        if (get().getThemes() != null)themes.addAll(get().getThemes());
        for (var style : themes) {
            if (style.getUrl() != null && !style.getUrl().isBlank()){
                if (!style.getUrl().startsWith("//") && style.getUrl().startsWith("http://") && style.getUrl().startsWith("https://"))
                {
                    styles.add(CdnAPI.renderUrl(style.getUrl()));
                }
                else
                {
                    styles.add(style.getUrl());
                }
            }
            else
            {
                styles.add(CdnAPI.renderUrl("css/%s@v%s/%s".formatted(style.getName(), style.getVersion(), MoreObjects.firstNonNull(style.getFileName(), "master.css"))));
            }
        }
        SiteImpl.of(get()).setStyles(styles);
    }

    public void setBaseUrlFromRequest(HttpServletRequest request) {
        var proto = request.getHeader("X-Forwarded-Proto");
        var host = request.getHeader("X-Forwarded-Host");
        var port = request.getHeader("X-Forwarded-Port");
        List<String> values = new ArrayList<>();
        var headers = Collections.list(request.getHeaderNames());

        for (var header : headers) {
            values.add("'%s'=\"%s\"".formatted(header, request.getHeader(header)));
        }

        LOGGER.debug("Enumerate all values: {}", String.join(", ", values));

        if (proto != null && host != null) {
            StringBuilder proxyHeader = new StringBuilder();
            proxyHeader.append(proto).append("://");
            proxyHeader.append(host);
            if(port != null) {
                if(!(proto.equals("http") && port.equals("4000")) && !(proto.equals("http") && port.equals("80")) && !(proto.equals("https") && port.equals("443"))) {
                    proxyHeader.append(":").append(port);
                }
            }
            setSiteUrl(proxyHeader.toString());
        } else {
            String baseUrl = request.getRequestURL().toString();
            setSiteUrl(baseUrl.substring(0, baseUrl.length() - request.getRequestURI().length()));
        }
    }
    @Override @SuppressWarnings("unchecked")
    public  Class<SiteImpl> getItemClass() {
        return SiteImpl.class;
    }

    @Override
    public String getDefaultConfig() {
        return new Gson().toJson(new SiteImpl());
    }
}
