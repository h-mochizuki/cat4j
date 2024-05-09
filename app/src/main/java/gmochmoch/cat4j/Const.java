package gmochmoch.cat4j;

/**
 * 定数を定義します
 */
public interface Const {

    /** 改行文字 */
    String LINE_SEPARATOR = "\n";
    /** 空文字 */
    String EMPTY = "";
    /** 標準入力用パラメータ */
    String STD_INPUT_PARAM = "-";
    /** 短いオプション用の節頭句 */
    String SHORT_DASH = "-";
    /** 長いオプション用の節頭句 */
    String LONG_DASH = "--";
    /** 行末尾表記文字 */
    String VISIBLE_LINE_END = "$";
    /** 行末尾表記文字(置換用) */
    String LITERAL_LINE_END = "\\" + VISIBLE_LINE_END;
    /** タブ表記文字 */
    String VISIBLE_TAB = "^I";
    /** 復帰文字表記文字 */
    String VISIBLE_LF = "^M";
    /** バックスペース表記文字 */
    String VISIBLE_BACKSPACE = "^H";
    /** 改ページ表記文字 */
    String VISIBLE_PAGE_BREAK = "^L";
}
