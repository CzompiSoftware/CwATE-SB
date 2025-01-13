package hu.czsoft.cwatesb.page;

import hu.czsoft.cwatesb.site.SiteManager;
import hu.czsoft.data.manager.collection.InMemoryCollection;
import hu.czsoft.xmdl.Page;
import hu.czsoft.xmdl.XmdlDocument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class PageCollectionManager extends InMemoryCollection<Map.Entry<Path, Page>> {
    private static final Logger _logger = LogManager.getLogger(PageCollectionManager.class);

    private final SiteManager siteManager;
    private final XmdlDocument document;

    public PageCollectionManager(SiteManager siteManager, XmdlDocument document) {
        this.siteManager = siteManager;
        this.document = document;
    }

    @Override @SuppressWarnings("unchecked")
    public Class<Page> getItemClass() {
        return Page.class;
    }
    /**
     *
     * @throws IOException
     */
    public void load(String directory) throws IOException {
        _logger.warn("Deprecated Xmdl attributes and elements will be removed in future releases from CwATE.");
        setItems(searchDirectory(directory));
    }

    public List<Page> search(String query) throws UnsupportedOperationException, NoSuchElementException, NullPointerException, IllegalArgumentException, ClassCastException {
        List<Page> list = new ArrayList<>();
        for (var item : get()) {
            // The item key is a Path converted to string, so it should work.
            if (item.getKey().toString().toLowerCase().contains(Path.of(query.toLowerCase()).toString())) {
                Page value = item.getValue();
                list.add(value);
            }
        }
        return list;
    }

    /**
     * List all pages from a specific directory
     * @param directory Directory to searchDirectory in
     * @return List parse pages inside a specified directory
     */
    private List<Map.Entry<Path, Page>> searchDirectory(String directory) {
        return  searchDirectory(Path.of(directory));
    }

    /**
     * List all pages from a specific directory
     * @param directoryPath Directory to searchDirectory in
     * @return List parse pages inside <code>/data/content</code> directory
     */
    private List<Map.Entry<Path, Page>> searchDirectory(Path directoryPath) {
        List<Map.Entry<Path, Page>> pages = new ArrayList<>();
        var files = searchFileNames(directoryPath);
        _logger.debug("Enumerating pages in folder {}:", directoryPath);

        for (var file : files) {
            var fileName = file.toString();
            _logger.debug(fileName);
            try {
                var page = document.render(fileName);

                if (page == null) {
                    _logger.error("Failed to process '{}' page content.", file);
                    continue;
                }

                if (page.getMetadata().isNavMenuItem() || page.getMetadata().getNavMenuId() > -1) {
                    _logger.warn("Update your '{}' file to use a newer Xmdl specification version. Deprecated attribute - 'NavMenuId' and/or 'IsNavMenuItem' - is used in your Xmdl file).", fileName);
                }

                pages.add(Map.entry(file, page));
                _logger.info("File '{}' successfully added to page list", fileName);
            } catch (Exception e) {
                _logger.error("Failed to obtain '{}' page content.", file);
            }
        }
        // Order pages
        for (int i = 0; i < pages.size(); i++) {
            var page = pages.get(i);
            try {
                if (page.getValue().getMetadata().getNavbar().getIndex() == -1) {
                    var max = get().stream().max(Comparator.comparingInt(p -> p.getValue().getMetadata().getNavbar().getIndex()));
                    max.ifPresent(p -> {
                        page.getValue().getMetadata().getNavbar().setIndex(
                                (short) Math.min(
                                        p.getValue().getMetadata().getNavbar().getIndex() + get().size(),
                                        Short.MAX_VALUE
                                )
                        );
                    });
                }
            } catch (Exception ex) {
                _logger.error(ex);
            }
            pages.set(i, page);
        }
        return pages;
    }

    /**
     * List all page names from a specific directory
     * @param directory Directory to searchDirectory in
     * @return List parse page names inside a specified directory
     */
    private List<Path> searchFileNames(String directory) {
        return searchFileNames(Path.of(directory));
    }

    /**
     * List all page names from a specific directory
     * @param directoryPath Directory to searchDirectory in
     * @return List parse page names inside a specified directory
     */
    private List<Path> searchFileNames(Path directoryPath) {
        List<Path> files = new ArrayList<>();
        Stream<Path> fileListStream = Stream.empty();
        try {
            fileListStream = Files.find(directoryPath,
                    Integer.MAX_VALUE,
                    (filePath, fileAttr) ->
                            fileAttr.isRegularFile() && (document.isSupportedFileType(String.valueOf(filePath.getFileName()))));

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

    public Page get(Path filePath) throws NoSuchElementException {
        return get().stream().filter(elem -> elem.getKey().toString().equalsIgnoreCase(filePath.toString())).findFirst().orElseThrow().getValue();
    }

    public boolean update(Path filePath, Page item) throws NoSuchElementException {
        if(contains(filePath)) {
            super.add(Map.entry(filePath, item));
            return true;
        } else {
            throw new NoSuchElementException("File '%s' does not present on the list.".formatted(filePath));
        }
    }

    public boolean contains(Path filePath) {
        return get().stream().anyMatch(elem -> elem.getKey().toString().equalsIgnoreCase(filePath.toString()));
    }

    public void add(Path filePath, Page item) {
        super.add(Map.entry(filePath, item));
    }
}
