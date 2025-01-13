package org.commonmark.ext.czsoft.xmdl.lua.inline;

import org.commonmark.ext.czsoft.xmdl.XmdlParser;
import org.commonmark.ext.czsoft.xmdl.lua.LuaBlock;
import org.commonmark.ext.czsoft.xmdl.lua.lre.LuaRuntime;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

public class InlineLuaHtmlNodeRenderer extends InlineLuaNodeRenderer {

    private final HtmlNodeRendererContext context;
    private final HtmlWriter html;
    private final LuaRuntime luaRuntime = LuaRuntime.create();
    private final XmdlParser xmdlParser = new XmdlParser();


    public InlineLuaHtmlNodeRenderer(HtmlNodeRendererContext context) {
        this.context = context;
        this.html = context.getWriter();
    }

    @Override
    public void render(Node node) {
        LuaBlock nb = (LuaBlock) node;
        if (nb.getCode() != null) {
            var result = luaRuntime.execute(nb.getCode(), nb.getType());
            html.text(xmdlParser.render(result));
            xmdlParser.close();
        }
    }
}
