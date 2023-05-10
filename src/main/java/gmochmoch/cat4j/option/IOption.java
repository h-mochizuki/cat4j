package gmochmoch.cat4j.option;

/**
 * オプションの基底インターフェース
 */
public interface IOption {

    /**
     * オプションを初期化します
     *
     * @param context オプション間で共有する情報
     */
    default void init(OptionContext context) {
    }

    /**
     * 対象を変換して返します
     *
     * @param text    対象文字列
     * @param context オプション間で共有する情報
     * @return 変換後の文字列
     */
    default String convert(String text, OptionContext context) {
        return text;
    }
}
