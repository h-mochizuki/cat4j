package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.option.IOption;
import gmochmoch.cat4j.option.OptionContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 連続した空行を一つにまとめます
 */
public class SqueezeBlankOption implements IOption {

    // 改行コードのパターン
    private static final Pattern LINE_PATTERN = Pattern.compile("\r\n|\r|\n");

    /**
     * 連続した空行を一つにまとめます
     *
     * @param text    対象文字列
     * @param context オプション間で共有する情報
     * @return 変換後の文字列
     */
    @Override
    public String transform(String text, OptionContext context) {
        StringBuilder builder = new StringBuilder();
        Matcher matcher = LINE_PATTERN.matcher(text);
        int pos = 0;
        boolean keep = false;
        while (matcher.find()) {
            String line = text.substring(pos, matcher.start());
            pos = matcher.end();
            if (line.isEmpty()) {
                if (keep) {
                    continue;
                }
                keep = true;
            } else {
                builder.append(line);
                keep = false;
            }
            builder.append(matcher.group());
        }
        if (pos < text.length()) {
            String line = text.substring(pos);
            if (!keep || !line.isEmpty()) {
                builder.append(line);
            }
        }
        return builder.toString();
    }
}
