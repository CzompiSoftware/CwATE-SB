package org.commonmark.ext.czsoft.xmd.lua.inline;

import org.commonmark.ext.czsoft.xmd.lua.LuaBlock;
import org.commonmark.ext.czsoft.xmd.lua.LuaType;
import org.commonmark.node.CustomNode;
import org.commonmark.node.Delimited;
import org.commonmark.parser.delimiter.DelimiterProcessor;
import org.commonmark.parser.delimiter.DelimiterRun;

public class InlineLua extends LuaBlock implements Delimited {
    private final static String BLOCK_START = "@lua{#";
    private final static String BLOCK_END = "#}";

    public InlineLua() {
        super(LuaType.INLINE);
    }

    @Override
    public String getOpeningDelimiter() {
        return BLOCK_START;
    }

    @Override
    public String getClosingDelimiter() {
        return BLOCK_END;
    }
}
