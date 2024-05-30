package org.commonmark.ext.czsoft.xmd.lua.inline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.commonmark.ext.czsoft.xmd.lua.LuaBlock;
import org.commonmark.ext.czsoft.xmd.lua.LuaType;
import org.commonmark.ext.czsoft.xmd.lua.block.LuaBlockParser;
import org.commonmark.node.Block;
import org.commonmark.parser.block.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LuaInlineParser extends AbstractBlockParser {
    private static Logger LOGGER = LogManager.getLogger(LuaInlineParser.class);
    private final static String BLOCK_START = "@lua{#";
    private final static String BLOCK_END = "#}";
    private static final Pattern LUA_CODE = Pattern
            .compile("(@lua\\{#)(.*)(#})", Pattern.LITERAL | Pattern.CASE_INSENSITIVE);
    private static final Pattern ALERT_CONTENT_LINE = Pattern.compile("\\s*]\\s(.*)");
    private InlineLua block;

    public LuaInlineParser() {
        this.block = new InlineLua();
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean canContain(Block block) {
        return block != null && !InlineLua.class.isAssignableFrom(block.getClass());
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState parserState) {
        return BlockContinue.none();
    }

    public static class Factory extends AbstractBlockParserFactory {
        private static Logger LOGGER = LogManager.getLogger(Factory.class);

        @Override
        public BlockStart tryStart(ParserState parserState, MatchedBlockParser matchedBlockParser) {
            CharSequence fullLine = parserState.getLine().getContent();
            String currentLineStr = fullLine.toString();

            if (!currentLineStr.contains(BLOCK_START) || !currentLineStr.contains(BLOCK_END)) {
                return BlockStart.none();
            }

            int col = parserState.getColumn();
            int indent = parserState.getIndent();
            int matchedIndex = Math.max(0, currentLineStr.indexOf(BLOCK_START) + BLOCK_START.length());
            LOGGER.debug(currentLineStr);
            return BlockStart
                    .of(new LuaBlockParser(LuaType.BLOCK, currentLineStr.substring(matchedIndex)))
                    .atColumn(col + indent + matchedIndex);
        }
    }
}
