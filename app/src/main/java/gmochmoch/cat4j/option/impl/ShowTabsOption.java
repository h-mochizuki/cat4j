package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.Const;
import gmochmoch.cat4j.option.IOption;

/**
 * タブを {@link Const#VISIBLE_TAB} で表示します
 */
public class ShowTabsOption implements IOption {

    /**
     * タブを {@link Const#VISIBLE_TAB} に変更して返します
     *
     * @param text 対象文字列
     * @return 変換後文字列
     */
    @Override
    public String convert(String text) {
        return text.replaceAll("(?m)\t", Const.VISIBLE_TAB);
    }
}
