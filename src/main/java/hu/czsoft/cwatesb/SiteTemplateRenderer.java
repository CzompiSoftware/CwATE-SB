package hu.czsoft.cwatesb;

import hu.czsoft.cwatesb.model.Engine;
import hu.czsoft.cwatesb.model.Page;
import hu.czsoft.cwatesb.model.Site;
import org.springframework.ui.Model;

import java.util.List;

public final class SiteTemplateRenderer {

    public static String renderDecoratedLayout(String fileName, Engine engine, Site site, List<Page> pages, Model model) {
        model.addAttribute("pageList", pages.stream().sorted().toList());
        return renderLayout("decoratedLayout", fileName, engine, site, pages, model);
    }

    public static String renderMainLayout(String fileName, Engine engine, Site site, List<Page> pages, Model model) {
        return renderLayout("mainLayout", fileName, engine, site, pages, model);
    }

    private static String renderLayout(String layout, String fileName, Engine engine, Site site, List<Page> pages, Model model) {
        var data = new XmdHandler();
        var page = data.renderFile(fileName, site.getBaseUrl());
        if(page.getMetadata().getUrl() == null || page.getMetadata().getUrl().isEmpty()) page.getMetadata().setUrl(fileName);


        model.addAttribute("engine", engine);
        model.addAttribute("site", site);
        model.addAttribute("page", page);

        return layout;
    }
}
