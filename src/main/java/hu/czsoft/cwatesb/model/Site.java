package hu.czsoft.cwatesb.model;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class Site {
    private static final Logger LOGGER = LogManager.getLogger(Site.class);

    private String id;

    private String defaultLang;

    private String shortName;

    private String name;

    private String themeColor;

    @Setter
    private String baseUrl;

    private String cdnUrl;

    private CopyrightHolder copyrightHolder;

    private List<Product> products;

    private List<String> styles;


    public Site(String id, String shortName, String name, String defaultLang, String themeColor, String baseUrl, String cdnUrl, CopyrightHolder copyrightHolder, List<Product> products, List<String> styles) {
        this.id = id;
        this.shortName = shortName;
        this.name = name;
        this.defaultLang = defaultLang;
        this.themeColor = themeColor;
        this.baseUrl = baseUrl;
        this.cdnUrl = cdnUrl;
        this.copyrightHolder = copyrightHolder;
        this.products = products;
        this.styles = styles;
    }

    public Site(String id, String shortName, String name, String defaultLang, String themeColor, String cdnUrl, CopyrightHolder copyrightHolder, List<Product> products, List<String> styles) {
        this(id, shortName, name, defaultLang, themeColor, null, cdnUrl, copyrightHolder, products, styles);
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

        LOGGER.info("Enumerate all values: {}", String.join(", ", values));

        if (proto != null && host != null) {
            StringBuilder proxyHeader = new StringBuilder();
            proxyHeader.append(proto).append("://");
            proxyHeader.append(host);
            if(port != null) {
                if(!(proto.equals("http") && port.equals("4000")) && !(proto.equals("http") && port.equals("80")) && !(proto.equals("https") && port.equals("443"))) {
                    proxyHeader.append(":").append(port);
                }
            }
            setBaseUrl(proxyHeader.toString());
        } else {
            String baseUrl = request.getRequestURL().toString();
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - request.getRequestURI().length());
        }
    }
}
