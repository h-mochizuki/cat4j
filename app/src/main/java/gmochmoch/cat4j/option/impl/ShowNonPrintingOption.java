package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.Const;
import gmochmoch.cat4j.option.IOption;

/**
 * 不可視の文字を特殊文字に置換します
 */
public class ShowNonPrintingOption implements IOption {

    /**
     * 不可視の文字を特殊文字に置き換えて返します
     * @param text 対象文字列
     * @return 変換後の文字列
     */
    @Override
    public String convert(String text) {
        return text
                .replaceAll("(\r)", Const.VISIBLE_LF)
                .replaceAll("(\b)", Const.VISIBLE_BACKSPACE)
                .replaceAll("(\f)", Const.VISIBLE_PAGE_BREAK);
    }
}
