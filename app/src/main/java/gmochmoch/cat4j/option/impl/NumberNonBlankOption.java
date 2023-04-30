package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.option.IOption;
import gmochmoch.cat4j.option.Options;

/**
 * 空行では行番号を表示しないようにします
 * <p/>
 * このクラスは {@link NumberOption} に依存します
 * @see NumberOption#setSkipEmptyLine(boolean)
 */
public class NumberNonBlankOption implements IOption {

    /**
     * {@link NumberOption} の空行で行番号表示をスキップする設定を有効にします
     * @see NumberOption#setSkipEmptyLine(boolean)
     */
    @Override
    public void init() {
        NumberOption option = (NumberOption) Options.NUMBER.option;
        option.setSkipEmptyLine(true);
    }
}
