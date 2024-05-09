package gmochmoch.cat4j.option;

/**
 * オプション間で共有する情報を保持します
 */
public class OptionContext {

    // 空行では行番号表示をスキップするか
    private boolean skipLineNumberOnEmptyLine = false;
    // 次も同じ行の処理が続くか
    private boolean continueOnSameLine = false;
    // 行番号
    private long lineNumber = 0;

    /**
     * 空行では行番号表示をスキップするかを返します
     *
     * @return true:スキップする<br/>false:スキップしない
     */
    public boolean isSkipLineNumberOnEmptyLine() {
        return skipLineNumberOnEmptyLine;
    }

    /**
     * 空行では行番号表示をスキップするかを設定します
     *
     * @param skipLineNumberOnEmptyLine true:スキップする/false:スキップしない
     */
    public void setSkipLineNumberOnEmptyLine(boolean skipLineNumberOnEmptyLine) {
        this.skipLineNumberOnEmptyLine = skipLineNumberOnEmptyLine;
    }

    /**
     * 次も同じ行の処理が続くかを返します
     *
     * @return true:同じ行の処理が続く<br/>false:違う行の処理
     */
    public boolean isContinueOnSameLine() {
        return continueOnSameLine;
    }

    /**
     * 次も同じ行の処理が続くかを設定します
     *
     * @param continueOnSameLine true:同じ行の処理が続く/false:違う行の処理
     */
    public void setContinueOnSameLine(boolean continueOnSameLine) {
        this.continueOnSameLine = continueOnSameLine;
    }

    /**
     * インクリメントした行番号を返します
     *
     * @return 行番号
     */
    public long incrementLineNumber() {
        return ++lineNumber;
    }
}
