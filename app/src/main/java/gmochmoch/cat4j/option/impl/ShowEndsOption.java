package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.Const;
import gmochmoch.cat4j.option.IOption;

/**
 * 行末に {@link Const#VISIBLE_LINE_END} を表示します
 */
public class ShowEndsOption implements IOption {

    /**
     * 行末に {@link Const#VISIBLE_LINE_END} を追加して返します
     *
     * @param text 対象文字列
     * @return 変換後文字列
     */
    @Override
    public String convert(String text) {
        return text.replaceAll("(\r?\n)", Const.LITERAL_LINE_END + "$1");
    }
}
