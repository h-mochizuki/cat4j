package gmochmoch.cat4j.option;

/**
 * オプションの基底インターフェース
 */
public interface IOption {

    /**
     * オプションを初期化します
     */
    default void init() {
    }

    /**
     * 対象を変換して返します
     *
     * @param text 対象文字列
     * @return 変換後の文字列
     */
    default String convert(String text) {
        return text;
    }
}
