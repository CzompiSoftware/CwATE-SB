package org.commonmark.ext.czsoft.xmd.lua.block;

import org.commonmark.ext.czsoft.xmd.lua.LuaBlock;
import org.commonmark.ext.czsoft.xmd.lua.LuaType;
import org.commonmark.node.Block;
import org.commonmark.parser.block.*;

import java.util.logging.Logger;

public class LuaBlockParser extends AbstractBlockParser {
    private final static String BLOCK_START = "@lua{>";
    private final static String BLOCK_END = "<}";
    private final LuaType type;
    private LuaBlock block;
    private String code;

    private static boolean isStarted = false;
    private boolean isEndReached = false;
    public LuaBlockParser(LuaType type, String codeStart) {
        this.type = type;
        this.block = new LuaBlock(type);
        code = codeStart + "\r\n";
        isStarted = true;
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean canContain(Block block) {
        return block != null && !LuaBlock.class.isAssignableFrom(block.getClass());
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState parserState) {
        if (!isStarted) {
            return BlockContinue.none();
        }
        CharSequence fullLine = parserState.getLine().getContent();
        CharSequence currentLine = fullLine.subSequence(parserState.getColumn() + parserState.getIndent(), fullLine.length());
        String currentLineStr = currentLine.toString();

        if (currentLineStr.contains(BLOCK_END)) {
            isEndReached = true;
        }

        String debugStr = "LuaBlockParser::tryContinue{";

        int col = parserState.getColumn();
        int indent = parserState.getIndent();
        int matchedIndex = 0;//Math.max(0, currentLineStr.indexOf(BLOCK_END)-1);
        code += currentLineStr + "\r\n";

        debugStr += "isStarted=" + (isStarted ? "true": "false") + ",";
        debugStr += "isEndReached=" + (isEndReached ? "true": "false") + ",";

        if (isStarted && isEndReached) {
            isStarted = false;
            isEndReached = false;
            code = code.substring(0, code.lastIndexOf("\r\n"));
            block.setCode(code.substring(code.startsWith(BLOCK_START) ? BLOCK_START.length(): 0, code.endsWith(BLOCK_END) ? code.length() - BLOCK_END.length() : 0).trim());
            return BlockContinue.none();
        }

        debugStr += "currentLineStr=\"" +currentLineStr + "\"";
        debugStr += "}";
        Logger.getLogger("LuaBlockParser").info(debugStr);
        return BlockContinue.atColumn(col + indent + matchedIndex);
    }

    public static class Factory extends AbstractBlockParserFactory {

        @Override
        public BlockStart tryStart(ParserState parserState, MatchedBlockParser matchedBlockParser) {
            CharSequence fullLine = parserState.getLine().getContent();
            CharSequence line = fullLine.subSequence(parserState.getColumn(), fullLine.length());
            String currentLineStr = line.toString();

            if (!currentLineStr.contains(BLOCK_START)) {
                return BlockStart.none();
            }

            int col = parserState.getColumn();
            int indent = parserState.getIndent();
            int matchedIndex = Math.max(0, currentLineStr.indexOf(BLOCK_START) + BLOCK_START.length());

            return BlockStart
                    .of(new LuaBlockParser(LuaType.BLOCK, currentLineStr.substring(matchedIndex)))
                    .atColumn(col + indent + matchedIndex);
        }
    }
}
