package hu.czsoft.cwatesb;

import com.fasterxml.jackson.core.JsonProcessingException;
import hu.czsoft.cwatesb.page.Metadata;
import hu.czsoft.cwatesb.page.Page;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.commonmark.ext.czsoft.xmd.XmdParser;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class XmdlParser {
    private static final Logger _logger = LogManager.getLogger(XmdlParser.class);
    @Getter
    private static final XmdlParser instance = new XmdlParser();
    @Getter private final XmdParser xmdParser = new XmdParser();

    public Page renderFile(String filePath, String baseUrl) {
        if (filePath.startsWith("/")) filePath = filePath.substring(1);

        if (filePath.isEmpty()) {
            filePath = "index.xmdl";
        }

        if (filePath.toLowerCase().endsWith(".html")) filePath = filePath.substring(0, filePath.length() - ".html".length()) + ".xmdl";

        var fullFilePath = (TemplatingEngineApplication.CONTENT_DIRECTORY + filePath);

        if (fullFilePath.toLowerCase().endsWith("/") || !isSupportedFileType(fullFilePath)) {
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

        return Page.of(page, baseUrl);
    }

    private Page parsePage(String rawContent) {
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
        //var content = xmdParser.render(rawContent);
        return new Page(metadata, rawContent, sha256);
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

    /**
     * List all pages from a specific directory
     * @param directory Directory to search in
     * @return List of pages inside <code>/data/content</code> directory
     */
    public List<Page> enumeratePages(String directory) {
        return  enumeratePages(Path.of(directory));
    }

    /**
     * List all pages from a specific directory
     * @param directoryPath Directory to search in
     * @return List of pages inside <code>/data/content</code> directory
     */
    public List<Page> enumeratePages(Path directoryPath) {
        List<Page> pages = new ArrayList<>();
        var files = enumeratePagePaths(directoryPath);
        _logger.debug("Enumerating pages in folder {}:", directoryPath);

        for (var file : files) {
            var fileName = file.toString();
            _logger.debug(fileName);
            try {
                var data = Files.readString(Path.of(fileName), StandardCharsets.UTF_8);
                //_logger.debug(data);
                var page = XmdlParser.getInstance().parsePage(data);

                if (page == null) {
                    _logger.warn("Failed to of '{}' page's content.", file);
                    continue;
                }

                pages.add(page);
            } catch (IOException e) {
                _logger.error(e);
            }
        }
        try {
            pages.stream().filter(page -> page.getMetadata().getNavMenuId() == -1).forEach(page -> {
                var max = TemplatingEngineApplication.PAGE_MANAGER.get().stream().max(Comparator.comparingInt(p -> p.getMetadata().getNavMenuId()));
                max.ifPresent(p -> page.getMetadata().setNavMenuId((short) Math.min(p.getMetadata().getNavMenuId() + TemplatingEngineApplication.PAGE_MANAGER.get().size(), Short.MAX_VALUE)));
            });
        } catch (Exception ex) {
            _logger.error(ex);
        }
        return pages;
    }

    /**
     * List all page names from a specific directory
     * @param directory Directory to search in
     * @return List of page names inside a specified directory
     */
    public List<Path> enumeratePagePaths(String directory) {
        return enumeratePagePaths(Path.of(directory));
    }

    /**
     * List all page names from a specific directory
     * @param directoryPath Directory to search in
     * @return List of page names inside a specified directory
     */
    public List<Path> enumeratePagePaths(Path directoryPath) {
        List<Path> files = new ArrayList<>();
        Stream<Path> fileListStream = Stream.empty();
        try {
            fileListStream = Files.find(directoryPath,
                    Integer.MAX_VALUE,
                    (filePath, fileAttr) ->
                            fileAttr.isRegularFile() && (isSupportedFileType(String.valueOf(filePath.getFileName()))));

            files = fileListStream.toList();
        } catch (IOException | UncheckedIOException | SecurityException | IllegalArgumentException e) {
            _logger.error(e);
        } finally {
            if(fileListStream != null) {
                fileListStream.close();
            }
        }

        return files;
    }

    private boolean isSupportedFileType(String fileName) {
        return Stream.of(".md", ".xmd", ".xmdl")
                .anyMatch(fileName::endsWith);
    }

    private boolean isValidDoctype(String document) {
        return Stream.of("<!doctype cwctma-docs>", "<!doctype cwate>", "<!doctype xmd>", "<!doctype xmdl>")
                .anyMatch(item -> document.toLowerCase().startsWith(item));
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
