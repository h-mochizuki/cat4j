package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.Const;
import gmochmoch.cat4j.option.IOption;
import gmochmoch.cat4j.option.OptionContext;

/**
 * 不可視の文字を特殊文字に置換します
 */
public class ShowNonPrintingOption implements IOption {

    /**
     * 不可視の文字を特殊文字に置き換えて返します
     *
     * @param text    対象文字列
     * @param context オプション間で共有する情報
     * @return 変換後の文字列
     */
    @Override
    public String transform(String text, OptionContext context) {
        return text
                .replaceAll("(\r)", Const.VISIBLE_LF)
                .replaceAll("(\b)", Const.VISIBLE_BACKSPACE)
                .replaceAll("(\f)", Const.VISIBLE_PAGE_BREAK);
    }
}
