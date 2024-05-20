package org.commonmark.ext.czsoft.xmd.lua.block;

import org.commonmark.ext.czsoft.xmd.XmdParser;
import org.commonmark.ext.czsoft.xmd.lua.LuaBlock;
import org.commonmark.ext.czsoft.xmd.lua.lre.LuaRuntime;
import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Collections;
import java.util.Set;

public class LuaNodeRenderer implements NodeRenderer {
    private final HtmlNodeRendererContext context;
    private final HtmlWriter htmlWriter;
    private final LuaRuntime luaRuntime = LuaRuntime.create();
    private final XmdParser xmdParser = new XmdParser();

    public LuaNodeRenderer(HtmlNodeRendererContext context) {
        this.context = context;
        this.htmlWriter = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.singleton(LuaBlock.class);
    }

    @Override
    public void render(Node node) {
        LuaBlock nb = (LuaBlock) node;
        if (nb.getCode() != null) {
            var result = luaRuntime.execute(nb.getCode(), nb.getType());
            htmlWriter.raw(xmdParser.render(result));
            xmdParser.close();
        }
    }

}
