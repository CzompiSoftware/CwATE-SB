package org.commonmark.ext.czsoft.xmd.lua.inline;

import org.commonmark.ext.czsoft.xmd.XmdParser;
import org.commonmark.ext.czsoft.xmd.lua.LuaBlock;
import org.commonmark.ext.czsoft.xmd.lua.lre.LuaRuntime;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Collections;
import java.util.Map;

public class InlineLuaHtmlNodeRenderer extends InlineLuaNodeRenderer {

    private final HtmlNodeRendererContext context;
    private final HtmlWriter html;
    private final LuaRuntime luaRuntime = LuaRuntime.create();
    private final XmdParser xmdParser = new XmdParser();


    public InlineLuaHtmlNodeRenderer(HtmlNodeRendererContext context) {
        this.context = context;
        this.html = context.getWriter();
    }

    @Override
    public void render(Node node) {
        LuaBlock nb = (LuaBlock) node;
        if (nb.getCode() != null) {
            var result = luaRuntime.execute(nb.getCode(), nb.getType());
            html.text(xmdParser.render(result));
            xmdParser.close();
        }
    }
}
