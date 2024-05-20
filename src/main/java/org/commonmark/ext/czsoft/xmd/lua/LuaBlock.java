package org.commonmark.ext.czsoft.xmd.lua;

import lombok.Getter;
import lombok.Setter;
import org.commonmark.node.CustomBlock;

@Getter
public class LuaBlock extends CustomBlock {

    private final LuaType type;
    @Setter
    private String code;

    public LuaBlock(LuaType type) {
        this.type = type;
    }

}
