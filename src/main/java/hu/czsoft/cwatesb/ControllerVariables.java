package hu.czsoft.cwatesb;

import hu.czsoft.web.engine.EngineManager;
import hu.czsoft.cwatesb.page.PageCollectionManager;
import hu.czsoft.cwatesb.site.SiteManager;
import hu.czsoft.xmdl.XmdlDocument;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component("controllerVariables")
@Getter
public final class ControllerVariables {
    private final EngineManager engine;
    private final SiteManager site;
    private final XmdlDocument document;
    private final PageCollectionManager pageManager;

    public ControllerVariables() {
        this.engine = TemplatingEngineApplication.ENGINE_MANAGER;
        this.site = TemplatingEngineApplication.SITE_MANAGER;
        this.pageManager = TemplatingEngineApplication.PAGE_MANAGER;
        this.document = TemplatingEngineApplication.XMDL_DOCUMENT;
    }
    
}
