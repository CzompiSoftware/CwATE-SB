package hu.czsoft.cwatesb.model;

import com.google.common.base.MoreObjects;
import hu.czsoft.cwatesb.persistent.SiteConfig;

import java.util.Arrays;

public class CdnAPI {

    /**
     * @param loc Relative file location
     * @return
     */
    public static String renderUrl(String loc)
    {
        String[] locArr = loc.split("/");

        if (loc.contains("@")) { // Ex.: "css/proj@1.0/file.css"
            String builder = "";
//            builder += MoreObjects.firstNonNull(SiteConfig.getInstance().getCdnUrl(), "https://cdn.czsoft.hu/");
            builder += MoreObjects.firstNonNull(SiteConfig.getInstance().getCdnUrl(), "https://cdn.czsoft.dev/");
            builder += locArr[0] + "/";
            builder += locArr[1].toLowerCase()+"/";
            builder += String.join("/", Arrays.stream(locArr).skip(2).toList());
            return builder;
        }

        String builder = "";
//        builder += MoreObjects.firstNonNull(SiteConfig.getInstance().getCdnUrl(), "https://cdn.czsoft.hu/");
        builder += MoreObjects.firstNonNull(SiteConfig.getInstance().getCdnUrl(), "https://cdn.czsoft.dev/");
        builder += locArr[0] + "/";
        builder += locArr[1].toLowerCase()+"/";
        builder += locArr[2].toLowerCase()+"/";
        builder+=String.join("/", Arrays.stream(locArr).skip(3).toList());
        return builder;
    }

}
