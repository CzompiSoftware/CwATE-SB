package org.commonmark.ext.czsoft.xmd.lua.inline;

import org.commonmark.ext.czsoft.xmd.XmdParser;
import org.commonmark.ext.czsoft.xmd.lua.LuaBlock;
import org.commonmark.ext.czsoft.xmd.lua.lre.LuaRuntime;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;
import org.commonmark.renderer.text.TextContentNodeRendererContext;
import org.commonmark.renderer.text.TextContentWriter;

import java.util.Collections;
import java.util.Map;

public class InlineLuaTextContentNodeRenderer extends InlineLuaNodeRenderer {

    private final TextContentNodeRendererContext context;
    private final TextContentWriter textContent;
    private final LuaRuntime luaRuntime = LuaRuntime.create();
    private final XmdParser xmdParser = new XmdParser();

    public InlineLuaTextContentNodeRenderer(TextContentNodeRendererContext context) {
        this.context = context;
        this.textContent = context.getWriter();
    }

    @Override
    public void render(Node node) {
        LuaBlock nb = (LuaBlock) node;
        if (nb.getCode() != null) {
            var result = luaRuntime.execute(nb.getCode(), nb.getType());
            textContent.write(xmdParser.render(result));
            xmdParser.close();
        }
    }

}
