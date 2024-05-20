package org.commonmark.ext.czsoft.xmd;

import org.commonmark.ext.czsoft.xmd.alert.AlertBlockParser;
import org.commonmark.ext.czsoft.xmd.alert.AlertNodeRenderer;
import org.commonmark.ext.czsoft.xmd.lua.block.LuaBlockParser;
import org.commonmark.ext.czsoft.xmd.lua.block.LuaNodeRenderer;
import org.commonmark.Extension;
import org.commonmark.ext.czsoft.xmd.lua.inline.LuaInlineParser;
import org.commonmark.ext.czsoft.xmd.lua.inline.InlineLuaHtmlNodeRenderer;
import org.commonmark.internal.inline.InlineContentParser;
import org.commonmark.parser.InlineParser;
import org.commonmark.parser.InlineParserFactory;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class XmdExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    public static Extension create() {
        return new XmdExtension();
    }

    @Override
    public void extend(org.commonmark.parser.Parser.Builder parserBuilder) {
        parserBuilder.customBlockParserFactory(new AlertBlockParser.Factory());
        parserBuilder.customBlockParserFactory(new LuaBlockParser.Factory());
        parserBuilder.customBlockParserFactory(new LuaInlineParser.Factory());
    }

    @Override
    public void extend(org.commonmark.renderer.html.HtmlRenderer.Builder htmlBuilder) {
        htmlBuilder.nodeRendererFactory(AlertNodeRenderer::new);
        htmlBuilder.nodeRendererFactory(LuaNodeRenderer::new);
        htmlBuilder.nodeRendererFactory(InlineLuaHtmlNodeRenderer::new);
    }
}
