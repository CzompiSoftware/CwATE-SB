package org.commonmark.ext.czsoft.xmd.lua.inline;

import org.commonmark.node.Node;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.text.TextContentNodeRendererContext;
import org.commonmark.renderer.text.TextContentWriter;

import java.util.Collections;
import java.util.Set;

abstract class InlineLuaNodeRenderer implements NodeRenderer {

    @Override
    public Set<Class<? extends Node>> getNodeTypes() {
        return Collections.singleton(InlineLua.class);
    }
}
