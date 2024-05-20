package org.commonmark.ext.czsoft.xmd.alert;

import org.commonmark.node.Block;
import org.commonmark.parser.block.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlertBlockParser extends AbstractBlockParser {
    private static final Pattern ALERT_LINE = Pattern.compile("\\s*]>([a-zA-Z]*)<\\s(.*)");
    private static final Pattern ALERT_CONTENT_LINE = Pattern.compile("\\s*]\\s(.*)");
    private final AlertType type;
    private AlertBlock block;

    public AlertBlockParser(AlertType type) {
        this.type = type;
        this.block = new AlertBlock(type);
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    @Override
    public boolean canContain(Block block) {
        return block != null && !AlertBlock.class.isAssignableFrom(block.getClass());
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public BlockContinue tryContinue(ParserState parserState) {
        CharSequence fullLine = parserState.getLine().getContent();
        CharSequence currentLine = fullLine.subSequence(parserState.getColumn() + parserState.getIndent(), fullLine.length());
        String currentLineStr = fullLine.toString();
        if (!currentLineStr.contains("]") || currentLineStr.contains("]>")) {
            return BlockContinue.none();
        }
        //Matcher matcher = ALERT_CONTENT_LINE.matcher(currentLine);
        //if (matcher.matches()) {
            if(currentLineStr.contains("]") && !currentLineStr.contains("]>")) {
                int col = parserState.getColumn();
                int indent = parserState.getIndent();
                int matchedIndex = currentLineStr.indexOf(']') + 1;

                return BlockContinue.atColumn(col + indent + matchedIndex);
            }
        //}

        return BlockContinue.none();
    }

    public static class Factory extends AbstractBlockParserFactory {

        @Override
        public BlockStart tryStart(ParserState state, MatchedBlockParser matchedBlockParser) {
            CharSequence fullLine = state.getLine().getContent();
            CharSequence line = fullLine.subSequence(state.getColumn(), fullLine.length());
            Matcher matcher = ALERT_LINE.matcher(line);
            if (matcher.matches()) {
                return BlockStart
                        .of(new AlertBlockParser(AlertType.fromString(matcher.group(1))))
                        .atColumn(state.getColumn() + state.getIndent() + matcher.start(2));
            }
            return BlockStart.none();
        }
    }
}
