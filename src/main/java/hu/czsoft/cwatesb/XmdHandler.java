package hu.czsoft.cwatesb;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.czsoft.cwatesb.model.Metadata;
import hu.czsoft.cwatesb.model.Page;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.commonmark.ext.czsoft.xmd.XmdParser;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class XmdHandler {
    private static final Logger _logger = LogManager.getLogger(XmdHandler.class);
    @Getter
    private static final XmdHandler instance = new XmdHandler();
    private final XmdParser xmdParser = new XmdParser();

    public Page renderFile(String filePath, String baseUrl) {
        if (filePath.startsWith("/")) filePath = filePath.substring(1);

        if (filePath.isEmpty()) {
            filePath = "index.xmdl";
        }

        if (filePath.toLowerCase().endsWith(".html")) filePath = filePath.substring(0, filePath.length() - ".html".length()) + ".xmdl";

        var fullFilePath = (TemplatingEngineApplication.CONTENT_DIRECTORY + filePath);

        boolean isValidFile = fullFilePath.toLowerCase().endsWith(".xmd") || fullFilePath.toLowerCase().endsWith(".xmdl") || fullFilePath.toLowerCase().endsWith(".html");

        if (fullFilePath.toLowerCase().endsWith("/") || !isValidFile) {
            if (Files.isDirectory(Path.of(fullFilePath))) {
                if(!fullFilePath.endsWith("/")) fullFilePath += "/";
                fullFilePath += "index.xmdl";
            }
        }
//        _logger.debug(fullFilePath);
        if (fullFilePath.toLowerCase().endsWith(".xmdl") && !Files.exists(Path.of(fullFilePath))) {
            fullFilePath = fullFilePath.substring(0, fullFilePath.length() - 1); //.xmdl -> .xmd
        }

        _logger.debug(fullFilePath);
        var fullPath = Path.of(fullFilePath);
        String rawContent = "";

        if (!Files.exists(fullPath)) {
            try {
                var resource = new ClassPathResource("static/404.xmd", this.getClass().getClassLoader());
                rawContent = resource.getContentAsString(StandardCharsets.UTF_8);
            } catch (IOException ex) {
                _logger.error(ex);
            }
        } else {
            try {
                rawContent = Files.readString(fullPath, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                _logger.error(ex);
            }
        }


        Page page = parsePage(rawContent);

        return Page.parse(page, baseUrl);
    }

    private Page parsePage(String rawContent) {
        if(rawContent == null) return null;

        if(rawContent.isEmpty()) return null;

        rawContent = rawContent.strip();

        if(!rawContent.toLowerCase().startsWith("<!doctype cwctma-docs>") && !rawContent.toLowerCase().startsWith("<!doctype cwate>")) {
            return null;
        }


        Metadata metadata = parseMetadata(rawContent);

        rawContent = rawContent.substring(metadata.getLength());
        rawContent = clearText(rawContent);
        var sha256 = DigestUtils.sha256Hex(rawContent);
        var content = xmdParser.render(rawContent);
        return new Page(metadata, content, sha256);
    }

    private String clearText(String rawContent) {
        if (rawContent.startsWith("\n")) {
            return clearText(rawContent.substring(1).strip());
        }
        if (rawContent.startsWith("\r\n")) {
            return clearText(rawContent.substring(2).strip());
        }
        return rawContent.strip();
    }

    private Metadata parseMetadata(String content) {
        String rawMetadata = content.substring(0, content.toLowerCase().indexOf("</metadata>") + "</metadata>".length());
        Metadata metadata = new Metadata();
        try {
            metadata = Metadata.parse(TemplatingEngineApplication.XML_MAPPER.readValue(rawMetadata, Metadata.class), rawMetadata.length());
//            _logger.debug(rawMetadata.replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t"));
            _logger.debug(metadata);
        } catch (JsonProcessingException e) {
            _logger.error(e);
        }
        return metadata;
    }

    public List<Page> enumeratePages() {
        List<Page> pages = new ArrayList<>();
        var files = enumeratePagePaths();
        _logger.debug("Enumerating pages in folder {}:", TemplatingEngineApplication.CONTENT_DIRECTORY);

        for (var file : files) {
            var fileName = TemplatingEngineApplication.CONTENT_DIRECTORY + file.toString();
            _logger.debug(fileName);
            try {
                var data = Files.readString(Path.of(fileName), StandardCharsets.UTF_8);
                //_logger.debug(data);
                var page = XmdHandler.getInstance().parsePage(data);

                if (page == null) {
                    _logger.warn("Failed to parse '{}' page's content.", file);
                    continue;
                }

                pages.add(page);
            } catch (IOException e) {
                _logger.error(e);
            }
        }
        try {
            pages.stream().filter(page -> page.getMetadata().getNavMenuId() == -1).forEach(page -> {
                var max = TemplatingEngineApplication.PAGE_LIST.stream().max(Comparator.comparingInt(p -> p.getMetadata().getNavMenuId()));
                max.ifPresent(p -> page.getMetadata().setNavMenuId((short) Math.min(p.getMetadata().getNavMenuId() + TemplatingEngineApplication.PAGE_LIST.size(), Short.MAX_VALUE)));
            });
        } catch (Exception ex) {
            _logger.error(ex);
        }
        return pages;
    }

    public List<Path> enumeratePagePaths() {
        List<Path> files = new ArrayList<>();

        try {
            files = Files.find(Paths.get(TemplatingEngineApplication.CONTENT_DIRECTORY),
                            Integer.MAX_VALUE,
                            (filePath, fileAttr) ->
                                    fileAttr.isRegularFile() && (filePath.getFileName().endsWith(".md") || filePath.getFileName().endsWith(".xmd") || filePath.getFileName().endsWith(".xmdl")))
                    .toList();
        } catch (IOException e) {
            _logger.error(e);
        }

        return files;
    }

}
