package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.option.IOption;
import gmochmoch.cat4j.option.OptionContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 行頭に行番号を表示します
 */
public class NumberOption implements IOption {

    // 改行コードのパターン
    private static final Pattern LINE_PATTERN = Pattern.compile("\r\n|\r|\n");
    // 変換テンプレート
    private static final String LINE_FORMAT = "%6d  %s";

    /**
     * 行頭に行番号を追加して返します
     *
     * @param text    対象文字列
     * @param context オプション間で共有する情報
     * @return 変換後文字列
     */
    @Override
    public String transform(String text, OptionContext context) {
        StringBuilder builder = new StringBuilder();
        Matcher matcher = LINE_PATTERN.matcher(text);
        int pos = 0;
        while (matcher.find()) {
            String line = text.substring(pos, matcher.start());
            if (context.isContinueOnSameLine()
                    || (line.isEmpty() && context.isSkipLineNumberOnEmptyLine())) {
                // 行の途中から始まったのでそのまま連結
                builder.append(line);
                context.setContinueOnSameLine(false);
            } else {
                builder.append(
                        LINE_FORMAT.formatted(context.incrementLineNumber(), line));
            }
            builder.append(matcher.group());
            pos = matcher.end();
        }
        if (pos < text.length()) {
            String line = text.substring(pos);
            // 最後に改行がない場合
            if (context.isContinueOnSameLine()
                    || (line.isEmpty() && context.isSkipLineNumberOnEmptyLine())) {
                // 行の途中から始まったのでそのまま連結
                builder.append(line);
            } else {
                builder.append(
                        LINE_FORMAT.formatted(context.incrementLineNumber(), line));
            }
            context.setContinueOnSameLine(true);
        }
        return builder.toString();
    }
}
