package hu.czsoft.cwatesb.controller;

import hu.czsoft.cwatesb.TemplatingEngineApplication;
import hu.czsoft.cwatesb.SiteTemplateRenderer;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class XmdController {
    Logger logger = LogManager.getLogger(XmdController.class);
    @GetMapping("/")
    public String handleIndex(HttpServletRequest request, Model model) {
        return processPath(request, "index.xmd", model);
    }

    private String processPath(HttpServletRequest request, String path, Model model) {
        if (TemplatingEngineApplication.SITE.getBaseUrl() == null) {
            TemplatingEngineApplication.SITE.setBaseUrlFromRequest(request);
        }
        return SiteTemplateRenderer.renderDecoratedLayout(path, TemplatingEngineApplication.ENGINE, TemplatingEngineApplication.SITE, TemplatingEngineApplication.PAGE_LIST, model);
    }

    @GetMapping("/**.xmd")
    public String handleXmd(HttpServletRequest httpServletRequest, Model model) {
        return handleRequest(httpServletRequest, model);
    }

    @GetMapping("/**.xmdl")
    public String handleXmdl(HttpServletRequest httpServletRequest, Model model) {
        return handleRequest(httpServletRequest, model);
    }

    @GetMapping("/**.html")
    public String handleHtml(HttpServletRequest httpServletRequest, Model model) {
        return handleRequest(httpServletRequest, model);
    }

    @GetMapping("/**")
    public String handleAll(HttpServletRequest httpServletRequest, Model model) {
        return handleRequest(httpServletRequest, model);
    }
    public String handleRequest(HttpServletRequest httpServletRequest, Model model) {
        var filePath = httpServletRequest.getServletPath();
        return processPath(httpServletRequest, filePath, model);
    }

    @GetMapping(value = "/favicon.ico")
    public ResponseEntity<Resource> handleFavicon(HttpServletRequest request) {
        if (TemplatingEngineApplication.SITE.getBaseUrl() == null) {
            TemplatingEngineApplication.SITE.setBaseUrlFromRequest(request);
        }
//        ClassPathResource classPathResource = new ClassPathResource("static/images/user-default.png");
//        InputStream in = classPathResource.getInputStream();
        var filePath = request.getServletPath();
        var fullPath = TemplatingEngineApplication.WORKING_DIRECTORY + filePath.substring(1);
        logger.info(fullPath);
        Path path = Path.of(fullPath);
        FileSystemResource resource = new FileSystemResource(path);
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(Files.probeContentType(path)))
                    .body(resource);
        } catch (IOException e) {
            logger.error(e);
            return ResponseEntity.notFound().build();
        }
    }

}
