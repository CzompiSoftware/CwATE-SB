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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.*;

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

        if (fullFilePath.toLowerCase().endsWith("/") || !(fullFilePath.toLowerCase().endsWith(".xmd") || fullFilePath.toLowerCase().endsWith(".xmdl") || fullFilePath.toLowerCase().endsWith(".html"))) {
            if (Files.isDirectory(Path.of(fullFilePath))) {
                if(!fullFilePath.endsWith("/")) fullFilePath += "/";
                fullFilePath += "index.xmdl";
            }
        }
        _logger.info(fullFilePath);
//        if (!Files.exists(Path.of(fullFilePath))) {
//            fullFilePath = fullFilePath.substring(0, fullFilePath.length() - 1); //.xmdl -> .xmd
//        }

        _logger.info(fullFilePath);
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

        if(!rawContent.toLowerCase().startsWith("<!doctype cwctma-docs>") && !rawContent.toLowerCase().startsWith("<!doctype cwate>")) return null;


        Metadata metadata = parseMetadata(rawContent);

        rawContent = rawContent.substring(metadata.getLength());
        while (rawContent.startsWith("\n")) {
            rawContent = rawContent.substring(1);
        }
        while (rawContent.startsWith("\r\n")) {
            rawContent = rawContent.substring(2);
        }
        var sha256 = DigestUtils.sha256Hex(rawContent);
        var content = xmdParser.render(rawContent);
        return new Page(metadata, content, sha256);
    }
    private Metadata parseMetadata(String content) {
        String rawMetadata = content.substring(0, content.toLowerCase().indexOf("</metadata>") + "</metadata>".length());
        Metadata metadata = new Metadata();
        try {
            metadata = Metadata.parse(TemplatingEngineApplication.XML_MAPPER.readValue(rawMetadata, Metadata.class), rawMetadata.length());
            _logger.info(rawMetadata.replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t"));
            _logger.info(metadata);
        } catch (JsonProcessingException e) {
            _logger.error(e);
        }
        return metadata;
    }

//    public List<Metadata> enumeratePageMetadata() {
//        List<Metadata> pages = new ArrayList<>();
//        var files = enumeratePageName();
//
//        for (var file : files) {
//            file = TemplatingEngineApplication.CONTENT_DIRECTORY + file;
//            _logger.info(file);
//            try {
//                var data = Files.readString(Path.of(file), StandardCharsets.UTF_8);
//                //_logger.info(data);
//                pages.add(XmdHandler.getInstance().parseMetadata(data));
//            } catch (IOException e) {
//                _logger.error(e);
//            }
//        }
//
//        return pages;
//    }

    public List<Page> enumeratePages() {
        List<Page> pages = new ArrayList<>();
        var files = enumeratePageName();

        for (var file : files) {
            file = TemplatingEngineApplication.CONTENT_DIRECTORY + file;
            _logger.trace(file);
            try {
                var data = Files.readString(Path.of(file), StandardCharsets.UTF_8);
                //_logger.info(data);
                var page = XmdHandler.getInstance().parsePage(data);

                if(page == null) continue;

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

    public List<String> enumeratePageName() {
        var folder = new File(TemplatingEngineApplication.CONTENT_DIRECTORY);
        FilenameFilter folderFilter = (dir, name) -> name.toLowerCase().endsWith(".xmd") || name.toLowerCase().endsWith(".xmdl");
        var files = folder.list(folderFilter);

        return stream(files != null ? files : new String[0]).toList();
    }

}
