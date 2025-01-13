package org.commonmark.ext.czsoft.xmdl;

import org.commonmark.ext.czsoft.xmdl.alert.AlertBlockParser;
import org.commonmark.ext.czsoft.xmdl.alert.AlertNodeRenderer;
import org.commonmark.ext.czsoft.xmdl.lua.block.LuaBlockParser;
import org.commonmark.ext.czsoft.xmdl.lua.block.LuaNodeRenderer;
import org.commonmark.Extension;
import org.commonmark.ext.czsoft.xmdl.lua.inline.LuaInlineParser;
import org.commonmark.ext.czsoft.xmdl.lua.inline.InlineLuaHtmlNodeRenderer;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class XmdlExtension implements Parser.ParserExtension, HtmlRenderer.HtmlRendererExtension {

    public static Extension create() {
        return new XmdlExtension();
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
