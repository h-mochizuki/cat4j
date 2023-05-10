package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.Const;
import gmochmoch.cat4j.exception.CancelException;
import gmochmoch.cat4j.option.IOption;
import gmochmoch.cat4j.option.OptionContext;
import gmochmoch.cat4j.option.Options;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * ヘルプを表示します
 */
public class HelpOption implements IOption {

    // ヘルプのフォーマット
    private static final String HELP_FORMAT = """
            Usage: cat4j [OPTION]... [FILE]...
            Concatenate FILE(s) to standard output.
            
            With no FILE, or when FILE is -, read standard input.
            
            %s
            
            Examples:
              cat f - g  Output f's contents, then standard input, then g's contents.
              cat        Copy standard input to standard output.""";
    // オプション説明のフォーマット
    private static final String DESC_FORMAT = "%5s %-20s %s";

    /**
     * バージョンを取得して処理をキャンセルさせます
     */
    @Override
    public void init(OptionContext context) {
        String desc = sortedOptions().stream().map(o -> {
            String so = Const.EMPTY;
            if (Objects.nonNull(o.shortOption)) {
                so = "-" + o.shortOption + ",";
            }
            String lo = Const.EMPTY;
            if (Objects.nonNull(o.longOption)) {
                lo = "--" + o.longOption;
            }
            return DESC_FORMAT.formatted(so, lo, o.description);
        }).collect(Collectors.joining(Const.LINE_SEPARATOR));
        throw new CancelException(HELP_FORMAT.formatted(desc));
    }

    /**
     * オプション定義を取得してオプションの文字でソートして返します
     *
     * @return ソート後のオプション定義一覧
     */
    private List<Options> sortedOptions() {
        List<Options> hasShort = new ArrayList<>();
        List<Options> noShort = new ArrayList<>();
        for (Options opt : Options.values()) {
            if (Objects.nonNull(opt.shortOption)) {
                hasShort.add(opt);
            } else {
                noShort.add(opt);
            }
        }
        hasShort.sort((o1, o2) -> {
            int i = o1.shortOption.compareToIgnoreCase(o2.shortOption);
            if (i == 0) {
                i = o1.shortOption.compareTo(o2.shortOption) * -1;
            }
            return i;
        });
        noShort.sort((o1, o2) -> {
            int i = o1.longOption.compareToIgnoreCase(o2.longOption);
            if (i == 0) {
                i = o1.longOption.compareTo(o2.longOption) * -1;
            }
            return i;
        });
        hasShort.addAll(noShort);
        return hasShort;
    }
}
