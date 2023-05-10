package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.option.IOption;
import gmochmoch.cat4j.option.OptionContext;

/**
 * 空行では行番号を表示しないようにします
 */
public class NumberNonBlankOption implements IOption {

    /**
     * 空行で行番号表示をスキップする設定を有効にします
     */
    @Override
    public void init(OptionContext context) {
        context.setSkipLineNumberOnEmptyLine(true);
    }
}
