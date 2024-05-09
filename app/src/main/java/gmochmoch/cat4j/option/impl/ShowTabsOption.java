package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.Const;
import gmochmoch.cat4j.option.IOption;
import gmochmoch.cat4j.option.OptionContext;

/**
 * タブを {@link Const#VISIBLE_TAB} で表示します
 */
public class ShowTabsOption implements IOption {

    /**
     * タブを {@link Const#VISIBLE_TAB} に変更して返します
     *
     * @param text    対象文字列
     * @param context オプション間で共有する情報
     * @return 変換後文字列
     */
    @Override
    public String transform(String text, OptionContext context) {
        return text.replaceAll("(?m)\t", Const.VISIBLE_TAB);
    }
}
