package hu.czsoft.xmdl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.commonmark.ext.czsoft.xmdl.XmdlParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class XmdlDocument {
    private static final Logger _logger = LogManager.getLogger(XmdlDocument.class);

    @Getter private final XmdlParser xmdlParser = new XmdlParser();

    private final String METADATA_TAG_START = "<metadata>";
    private final String METADATA_TAG_END = "</metadata>";

    private final XmlMapper xmlMapper;

    public XmdlDocument(XmlMapper xmlMapper) {
        this.xmlMapper = xmlMapper;
    }

    public Page render(String fileName) {
        fileName = checkFile(fileName);
        _logger.debug(fileName);
        var fullPath = Path.of(fileName);
        String rawContent = "";


        try {
            rawContent = Files.readString(fullPath, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            _logger.error(ex);
        }



        return parse(rawContent);
    }

    public Page parse(String rawContent) {
        if(rawContent == null) return null;

        if(rawContent.isEmpty()) return null;

        rawContent = rawContent.strip();

        if(!isValidDoctype(rawContent)) {
            return null;
        }


        Metadata metadata = parseMetadata(rawContent);

        rawContent = rawContent.substring(metadata.getLength());
        rawContent = clearText(rawContent);
        var sha256 = DigestUtils.sha256Hex(rawContent);
        //var content = xmdlParser.render(rawContent);
        return Page.of(metadata, rawContent, sha256);
    }

    private Metadata parseMetadata(String content) {
        String rawMetadata = content.substring(0, content.toLowerCase().indexOf(METADATA_TAG_END) + METADATA_TAG_END.length());
        Metadata metadata = new Metadata();
        try {
            metadata = Metadata.of(xmlMapper.readValue(rawMetadata, Metadata.class), rawMetadata.length());
            _logger.debug(rawMetadata.replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t"));
            _logger.debug(metadata);
        } catch (JsonProcessingException e) {
            _logger.error(e);
        }
        return metadata;
    }

    public boolean isSupportedFileType(String fileName) {
        return Stream.of(".md", ".xmd", ".xmdl")
                .anyMatch(fileName.toLowerCase()::endsWith);
    }

    private boolean isValidDoctype(String document) {
        return Stream.of("<!doctype cwctma-docs>", "<!doctype cwate>", "<!doctype xmd>", "<!doctype xmdl>")
                .anyMatch(document.toLowerCase()::startsWith);
    }

    private String checkFile(String fileName) {
        if (fileName.startsWith("/")) fileName = fileName.substring(1);

        if (fileName.isEmpty()) {
            fileName = "index.xmdl";
        }

        if (fileName.toLowerCase().endsWith(".html")) fileName = fileName.substring(0, fileName.length() - ".html".length()) + ".xmdl";

        if (fileName.toLowerCase().endsWith("/") || !isSupportedFileType(fileName)) {
            if (Files.isDirectory(Path.of(fileName))) {
                if(!fileName.endsWith("/")) fileName += "/";
                fileName += "index.xmdl";
            }
        }

        if (fileName.toLowerCase().endsWith(".xmdl") && !Files.exists(Path.of(fileName))) {
            fileName = fileName.substring(0, fileName.length() - 1); //.xmdl -> .xmd
        }

        return fileName;
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

}
