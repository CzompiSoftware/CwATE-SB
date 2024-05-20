package hu.czsoft.cwatesb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.reflect.ClassPath;
import hu.czsoft.cwatesb.model.Metadata;
import hu.czsoft.cwatesb.model.Page;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.commonmark.ext.czsoft.xmd.XmdParser;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XmdHandler {
    private static final Logger _logger = LogManager.getLogger(XmdHandler.class);
    @Getter
    private static final XmdHandler instance = new XmdHandler();
    private XmdParser xmdParser = new XmdParser();

    public Page renderFile(String filePath, String baseUrl) {
        if (filePath.startsWith("/")) filePath = filePath.substring(1);
        if (filePath.isEmpty()) filePath = "index.xmdl";
        if(filePath.endsWith(".html")) filePath = filePath.substring(0, filePath.length() - ".html".length()) + ".xmdl";
        var fullFilePath = (TemplatingEngineApplication.WORKING_DIRECTORY + filePath);

        if (!Files.exists(Path.of(fullFilePath))) {
            fullFilePath = fullFilePath.substring(0, fullFilePath.length()-1);
        }
        var fullPath = Path.of(fullFilePath);
        String rawContent;

        if (!Files.exists(fullPath)) {
            try {
                var resource = new ClassPathResource("static/404.xmd", this.getClass().getClassLoader());
                rawContent = resource.getContentAsString(StandardCharsets.UTF_8);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            try {
                rawContent = Files.readString(fullPath, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        Metadata metadata = parseMetadata(rawContent);

        rawContent = rawContent.substring(metadata.getLength());
        while (rawContent.startsWith("\n")) {
            rawContent = rawContent.substring(1);
        }
        while (rawContent.startsWith("\r\n")) {
            rawContent = rawContent.substring(2);
        }
        var content = xmdParser.render(rawContent);

        return new Page(metadata, content, baseUrl);
    }

    private Metadata parseMetadata(String content) {
        String rawMetadata = content.substring(0, content.toLowerCase().indexOf("</metadata>") + "</metadata>".length());
        Metadata metadata = new Metadata();
        try {
            metadata = TemplatingEngineApplication.XML_MAPPER.readValue(rawMetadata, Metadata.class);
            metadata = Metadata.parse(metadata, rawMetadata.length());
        } catch (JsonProcessingException e) {
            _logger.error(e);
        }
        return metadata;
    }

    public List<Metadata> enumeratePageMetadata() {
        List<Metadata> pages = new ArrayList<>();
        var files = enumeratePageName();

        for (var file : files) {
            file = TemplatingEngineApplication.WORKING_DIRECTORY + file;
            try {
                var data = Files.readString(Path.of(file), StandardCharsets.UTF_8);
                pages.add(XmdHandler.getInstance().parseMetadata(data));
            } catch (IOException e) {
                _logger.error(e);
            }
        }

        return pages;
    }
    public List<String> enumeratePageName() {
        var folder = new File(TemplatingEngineApplication.WORKING_DIRECTORY);
        FilenameFilter folderFilter = (dir, name) -> name.toLowerCase().endsWith(".xmd");
        var files = folder.list(folderFilter);

        return Arrays.stream(files != null ? files : new String[0]).toList();
    }

}
