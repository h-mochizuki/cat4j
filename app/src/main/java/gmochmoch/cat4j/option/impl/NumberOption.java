package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.option.IOption;

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
    // 行番号
    private long lineNumber = 1;
    // 行の処理が続いているか
    private boolean keep = false;
    // 空行では行番号表示をスキップするか
    private boolean skipEmptyLine = false;

    /**
     * 行頭に行番号を追加して返します
     *
     * @param text 対象文字列
     * @return 変換後文字列
     */
    @Override
    public String convert(String text) {
        StringBuilder builder = new StringBuilder();
        Matcher matcher = LINE_PATTERN.matcher(text);
        int pos = 0;
        while (matcher.find()) {
            String line = text.substring(pos, matcher.start());
            if (keep || (line.isEmpty() && skipEmptyLine)) {
                // 行の途中から始まったのでそのまま連結
                builder.append(line);
                keep = false;
            } else {
                builder.append(LINE_FORMAT.formatted(lineNumber++, line));
            }
            builder.append(matcher.group());
            pos = matcher.end();
        }
        if (pos < text.length()) {
            String line = text.substring(pos);
            // 最後に改行がない場合
            if (keep || (line.isEmpty() && skipEmptyLine)) {
                // 行の途中から始まったのでそのまま連結
                builder.append(line);
            } else {
                builder.append(LINE_FORMAT.formatted(lineNumber++, line));
            }
            keep = true;
        }
        return builder.toString();
    }

    /**
     * 空行で行番号表示をスキップするかを設定します
     *
     * @param flg true:スキップする<br/>false:スキップしない
     */
    public void setSkipEmptyLine(boolean flg) {
        skipEmptyLine = flg;
    }
}
