package org.commonmark.ext.czsoft.xmdl.lua.block;

import org.commonmark.ext.czsoft.xmdl.XmdlParser;
import org.commonmark.ext.czsoft.xmdl.lua.LuaBlock;
import org.commonmark.ext.czsoft.xmdl.lua.lre.LuaRuntime;
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
    private final XmdlParser xmdlParser = new XmdlParser();

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
            htmlWriter.raw(xmdlParser.render(result));
            xmdlParser.close();
        }
    }

}
