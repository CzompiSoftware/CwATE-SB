package hu.czsoft.cwatesb.controller;

import hu.czsoft.cwatesb.TemplatingEngineApplication;
import hu.czsoft.cwatesb.SiteTemplateRenderer;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class XmdController {
    @GetMapping("/")
    public String handleIndex(HttpServletRequest request, Model model) {
        return processPath(request, "index.xmd", model);
    }

    private String processPath(HttpServletRequest request, String path, Model model) {
        if (TemplatingEngineApplication.SITE.getBaseUrl() == null) {
            TemplatingEngineApplication.SITE.setBaseUrlFromRequest(request);
        }
        return SiteTemplateRenderer.renderDecoratedLayout(path, TemplatingEngineApplication.ENGINE, TemplatingEngineApplication.SITE, model);
    }

    @GetMapping("/**.xmd")
    public String handleXmd(HttpServletRequest httpServletRequest, Model model) {
        var filePath = httpServletRequest.getServletPath();
        return processPath(httpServletRequest,filePath, model);
    }
    @GetMapping(value = "/favicon.ico", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] handleFavicon(HttpServletRequest request) throws IOException {
        if (TemplatingEngineApplication.SITE.getBaseUrl() == null) {
            TemplatingEngineApplication.SITE.setBaseUrlFromRequest(request);
        }
//        ClassPathResource classPathResource = new ClassPathResource("static/images/user-default.png");
//        InputStream in = classPathResource.getInputStream();
        var filePath = request.getServletPath();
        var fullPath = "data/";
        if (filePath.startsWith("/")) filePath = filePath.substring(1);
        if (filePath.isEmpty()) filePath = "favicon.ico";
        fullPath = fullPath + filePath;

        File file = new File(fullPath);
        FileInputStream fis = new FileInputStream(file);

        return IOUtils.toByteArray(fis);
    }

}
