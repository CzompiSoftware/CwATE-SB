package hu.czsoft.cwatesb;

import hu.czsoft.xmdl.Page;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.NoSuchElementException;

@Component("layoutRenderer")@Getter
public final class SiteLayoutRenderer {
    private final ControllerVariables controllerVariables;

    public SiteLayoutRenderer(ControllerVariables controllerVariables) {
        this.controllerVariables = controllerVariables;
    }

    private static final Logger _logger = LogManager.getLogger(SiteLayoutRenderer.class);

    public String renderDecoratedLayout(String fileName, Model model) {
        model.addAttribute("pageList", controllerVariables.getPageManager().get().stream().map(Map.Entry::getValue).sorted().toList());
        return renderLayout("decoratedLayout", fileName, model);
    }

    public String renderMainLayout(String fileName, Model model) {
        return renderLayout("mainLayout", fileName, model);
    }

    private String renderLayout(String layout, String fileName, Model model) {
        if(fileName.startsWith("/")) fileName = fileName.substring(1);
        if(!fileName.startsWith(TemplatingEngineApplication.CONTENT_DIRECTORY)) {
            fileName = TemplatingEngineApplication.CONTENT_DIRECTORY + fileName;
        }
        Page page = Page.of();
        try {
            page = controllerVariables.getPageManager().get(Path.of(fileName));
        } catch (NoSuchElementException e) {
            page = printNotFoundPage();
        } catch (Exception e) {
            _logger.warn(e);
        }
        //if (page.getMetadata().getUrl() == null || page.getMetadata().getUrl().isEmpty()) page.getMetadata().setUrl(fileName);

        var renderedPage = Page.of(page.getMetadata(), controllerVariables.getDocument().getXmdParser().render(page.getContent()), page.getHash());

        model.addAttribute("engine", controllerVariables.getEngine().get());
        model.addAttribute("site", controllerVariables.getSite().get());
        model.addAttribute("page", renderedPage);

        return layout;
    }

    private Page printNotFoundPage() {

        try {
            var resource = new ClassPathResource("static/404.xmd", this.getClass().getClassLoader());
            var rawContent = resource.getContentAsString(StandardCharsets.UTF_8);
            return controllerVariables.getDocument().parse(rawContent);
        } catch (IOException ex) {
            _logger.error(ex);
        }
        return Page.of();
    }
}
