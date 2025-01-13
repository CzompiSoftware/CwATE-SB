package org.commonmark.ext.czsoft.xmdl.alert;

import lombok.Getter;
import org.commonmark.node.CustomBlock;

@Getter
public class AlertBlock extends CustomBlock {

    private final AlertType type;

    public AlertBlock(AlertType type) {
        this.type = type;
    }

}
