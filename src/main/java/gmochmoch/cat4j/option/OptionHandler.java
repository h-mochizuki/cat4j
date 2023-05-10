package gmochmoch.cat4j.option;

import java.util.*;

import static gmochmoch.cat4j.Const.EMPTY;
import static gmochmoch.cat4j.Const.LONG_DASH;
import static gmochmoch.cat4j.Const.SHORT_DASH;
import static gmochmoch.cat4j.Const.STD_INPUT_PARAM;

/**
 * オプションにあわせて処理を行うクラス
 */
public class OptionHandler {

    // オプションを除外した引数
    private final List<String> remains = new ArrayList<>();
    // 指定されたオプション
    private final List<IOption> options = new ArrayList<>();
    // オプション間で共有する情報
    private final OptionContext context = new OptionContext();

    /**
     * 起動引数からオプションとファイルなどを振り分けます
     *
     * @param args 起動引数
     */
    public void init(String... args) {
        Set<Options> optionSet = new LinkedHashSet<>();
        boolean toRemains = false;
        for (String arg : args) {
            if (toRemains) {
                remains.add(arg);
            } else if (EMPTY.equals(arg) || STD_INPUT_PARAM.equals(arg)) {
                remains.add(STD_INPUT_PARAM);
            } else if (LONG_DASH.equals(arg)) {
                toRemains = true;
            } else if (arg.startsWith(LONG_DASH)) {
                Options opt = Options.findByLongOption(arg.substring(2));
                optionSet.add(opt);
                optionSet.addAll(opt.delegates);
            } else if (arg.startsWith(SHORT_DASH)) {
                for (String shortOption : arg.substring(1).split(EMPTY)) {
                    Options opt = Options.findByShortOption(shortOption);
                    optionSet.add(opt);
                    optionSet.addAll(opt.delegates);
                }
            } else {
                remains.add(arg);
            }
        }
        // 変換時のオプション実行順をOptions登録の逆順にしたいのでソート
        options.addAll(optionSet.stream()
                .sorted((o1, o2) -> o1.compareTo(o2) * -1)
                .map(o -> o.option).toList());
        // 初期化処理は入力順で実行
        optionSet.stream().map(o -> o.option).forEach(o -> o.init(context));

        // 空の場合は標準入力を追加
        if (remains.isEmpty()) {
            remains.add(STD_INPUT_PARAM);
        }
    }

    /**
     * オプションを除いた引数を返します
     *
     * @return オプションを除いた引数
     */
    public List<String> getRemains() {
        return remains;
    }

    /**
     * 変換処理を実施し、結果を返します
     *
     * @param text 対象文字列
     * @return 変換後の文字列
     */
    public String convert(String text) {
        for (IOption opt : options) {
            text = opt.convert(text, context);
        }
        return text;
    }
}
