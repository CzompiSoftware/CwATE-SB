package org.commonmark.ext.czsoft.xmd.alert;

import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.HtmlNodeRendererContext;
import org.commonmark.renderer.html.HtmlWriter;

import java.util.Collections;
import java.util.Set;

public class AlertNodeRenderer implements NodeRenderer {
    private final HtmlNodeRendererContext context;
    private final HtmlWriter htmlWriter;
    public AlertNodeRenderer(HtmlNodeRendererContext context) {
        this.context = context;
        this.htmlWriter = context.getWriter();
    }

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.singleton(AlertBlock.class);
    }

    @Override
    public void render(Node node) {
        AlertBlock nb = (AlertBlock) node;

        htmlWriter.line();
        AlertType type = nb.getType();
        htmlWriter.tag("div", Collections.singletonMap("class", "alert alert-" + type.toString()));
        renderChildren(nb);
        htmlWriter.tag("/div");
        htmlWriter.line();
    }

    private void renderChildren(Node parent) {
        Node node = parent.getFirstChild();
        while (node != null) {
            Node next = node.getNext();
            context.render(node);
            node = next;
        }
    }
}
