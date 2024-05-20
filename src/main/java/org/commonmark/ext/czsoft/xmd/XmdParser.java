package org.commonmark.ext.czsoft.xmd;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.image.attributes.ImageAttributesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class XmdParser {
    private final Parser _parser;
    private final HtmlRenderer _renderer;

    public XmdParser() {
        List<Extension> extensions = List.of(
                TablesExtension.create(),
                StrikethroughExtension.create(),
                HeadingAnchorExtension.create(),
                TaskListItemsExtension.create(),
                ImageAttributesExtension.create(),
                XmdExtension.create()
        );
        _parser = Parser.builder().extensions(extensions).build();
        _renderer = HtmlRenderer.builder().extensions(extensions).build();

    }

    public String render(String content) {
        Node document = _parser.parse(content);
        return _renderer.render(document);
    }

    public void close() {
    }
}