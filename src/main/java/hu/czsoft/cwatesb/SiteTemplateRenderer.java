package hu.czsoft.cwatesb;

import hu.czsoft.cwatesb.model.Engine;
import hu.czsoft.cwatesb.model.Site;
import org.springframework.ui.Model;

public final class SiteTemplateRenderer {

    public static String renderDecoratedLayout(String fileName, Engine engine, Site site, Model model) {
        return renderLayout("decoratedLayout", fileName, engine, site, model);
    }

    public static String renderMainLayout(String fileName, Engine engine, Site site, Model model) {
        return renderLayout("mainLayout", fileName, engine, site, model);
    }

    private static String renderLayout(String layout, String fileName, Engine engine, Site site, Model model) {
        var data = new XmdHandler();
        var page = data.renderFile(fileName, site.getBaseUrl());

        model.addAttribute("engine", engine);
        model.addAttribute("site", site);
        model.addAttribute("page", page);

        return layout;
    }
}
