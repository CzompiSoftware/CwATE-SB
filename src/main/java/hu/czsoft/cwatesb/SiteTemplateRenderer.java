package hu.czsoft.cwatesb;

import hu.czsoft.cwatesb.engine.Engine;
import hu.czsoft.cwatesb.page.Page;
import hu.czsoft.cwatesb.site.SiteImpl;
import org.springframework.ui.Model;

import java.util.List;

public final class SiteTemplateRenderer {

    public static String renderDecoratedLayout(String fileName, Engine engine, SiteImpl site, List<Page> pages, Model model) {
        model.addAttribute("pageList", pages.stream().sorted().toList());
        return renderLayout("decoratedLayout", fileName, engine, site, model);
    }

    public static String renderMainLayout(String fileName, Engine engine, SiteImpl site, Model model) {
        return renderLayout("mainLayout", fileName, engine, site, model);
    }

    private static String renderLayout(String layout, String fileName, Engine engine, SiteImpl site, Model model) {
        var data = new XmdlParser();
        var page = data.renderFile(fileName, site.getSiteUrl());
        if (page.getMetadata().getUrl() == null || page.getMetadata().getUrl().isEmpty()) page.getMetadata().setUrl(fileName);

        var renderedPage = new Page(page.getMetadata(), data.getXmdParser().render(page.getContent()), page.getHash());

        model.addAttribute("engine", engine);
        model.addAttribute("site", site);
        model.addAttribute("page", renderedPage);

        return layout;
    }
}
