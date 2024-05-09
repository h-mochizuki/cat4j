package gmochmoch.cat4j.option;

import gmochmoch.cat4j.Const;
import gmochmoch.cat4j.option.impl.DelegateOption;
import gmochmoch.cat4j.option.impl.HelpOption;
import gmochmoch.cat4j.option.impl.NumberNonBlankOption;
import gmochmoch.cat4j.option.impl.NumberOption;
import gmochmoch.cat4j.option.impl.ShowEndsOption;
import gmochmoch.cat4j.option.impl.ShowNonPrintingOption;
import gmochmoch.cat4j.option.impl.ShowTabsOption;
import gmochmoch.cat4j.option.impl.SqueezeBlankOption;
import gmochmoch.cat4j.option.impl.VersionOption;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * オプション定義
 * <p/>
 * オプション指定文字列と実装を紐づけます
 */
public enum Options {

    /** バージョン */
    VERSION(null, "version", new VersionOption(), "output version information and exit"),
    /** ヘルプ */
    HELP(null, "help", new HelpOption(), "display this help and exit"),
    /** 不可視文字表示 */
    SHOW_NON_PRINTING("v", "show-nonprinting", new ShowNonPrintingOption(), "use ^ and M- notation, except for LFD and TAB"),
    /** 行末表示 */
    SHOW_ENDS("E", "show-ends", new ShowEndsOption(), "display " + Const.VISIBLE_LINE_END + " at end of each line"),
    /** タブ文字表示 */
    SHOW_TABS("T", "show-tabs", new ShowTabsOption(), "display TAB characters as " + Const.VISIBLE_TAB),
    /** 行番号表示 */
    NUMBER("n", "number", new NumberOption(), "number all output lines"),
    /** 行番号表示(空行除く): {@link #NUMBER} より後に来るようにしてください */
    NUMBER_NON_BLANK("b", "number-nonblank", new NumberNonBlankOption(), "number nonempty output lines, overrides -n", NUMBER),
    /** 連続した空行をまとめる: {@link #NUMBER} より後に来るようにしてください */
    SQUEEZE_BLANK("s", "squeeze-blank", new SqueezeBlankOption(), "suppress repeated empty output lines"),
    /** 不可視文字表示 + 行末表示 */
    VE("e", null, new DelegateOption(), "equivalent to -vE", SHOW_NON_PRINTING, SHOW_ENDS),
    /** 不可視文字表示 + タブ文字表示 */
    VT("t", null, new DelegateOption(), "equivalent to -vT", SHOW_NON_PRINTING, SHOW_TABS),
    /** 不可視文字表示 + 行末 + タブ文字表示 */
    ALL("A", "show-all", new DelegateOption(), "equivalent to -vET", SHOW_NON_PRINTING, SHOW_ENDS, SHOW_TABS),
    /** バッファなし(非対応) */
    NON_BUFFER("u", null, new DelegateOption(), "(ignored)"),
    ;

    /** 短いオプション */
    public final String shortOption;
    /** 長いオプション */
    public final String longOption;
    /** オプションの実装 */
    public final IOption option;
    /** 説明 */
    public final String description;
    /** 委譲先のオプション */
    public final List<Options> delegates;

    /**
     * コンストラクタ
     *
     * @param shortOption 短いオプション
     * @param longOption  長いオプション
     * @param option      オプションの実装
     * @param description 説明
     * @param delegates   委譲先のオプション
     */
    Options(String shortOption, String longOption, IOption option, String description, Options... delegates) {
        this.shortOption = shortOption;
        this.longOption = longOption;
        this.option = option;
        this.description = description;
        this.delegates = Arrays.asList(delegates);
    }

    /**
     * 短いオプションからオプション定義を返します
     *
     * @param option 短いオプション
     * @return オプション定義
     */
    public static Options findByShortOption(String option) {
        if (Objects.nonNull(option)) {
            for (Options o : Options.values()) {
                if (option.equals(o.shortOption)) {
                    return o;
                }
            }
        }
        throw new RuntimeException("invalid option -- '" + option + "'");
    }

    /**
     * 長いオプションからオプション定義を返します
     *
     * @param option 長いオプション
     * @return オプション定義
     */
    public static Options findByLongOption(String option) {
        if (Objects.nonNull(option)) {
            for (Options o : Options.values()) {
                if (option.equals(o.longOption)) {
                    return o;
                }
            }
        }
        throw new RuntimeException("unrecognized option '--" + option + "'");
    }
}
