package org.commonmark.ext.czsoft.xmdl.lua.inline;

import org.commonmark.ext.czsoft.xmdl.XmdlParser;
import org.commonmark.ext.czsoft.xmdl.lua.LuaBlock;
import org.commonmark.ext.czsoft.xmdl.lua.lre.LuaRuntime;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;
import org.commonmark.renderer.text.TextContentNodeRendererContext;
import org.commonmark.renderer.text.TextContentWriter;

public class InlineLuaTextContentNodeRenderer extends InlineLuaNodeRenderer {

    private final TextContentNodeRendererContext context;
    private final TextContentWriter textContent;
    private final LuaRuntime luaRuntime = LuaRuntime.create();
    private final XmdlParser xmdlParser = new XmdlParser();

    public InlineLuaTextContentNodeRenderer(TextContentNodeRendererContext context) {
        this.context = context;
        this.textContent = context.getWriter();
    }

    @Override
    public void render(Node node) {
        LuaBlock nb = (LuaBlock) node;
        if (nb.getCode() != null) {
            var result = luaRuntime.execute(nb.getCode(), nb.getType());
            textContent.write(xmdlParser.render(result));
            xmdlParser.close();
        }
    }

}
