package gmochmoch.cat4j.exception;

/**
 * ファイル処理を中断する例外です
 */
public class SkipFileException extends Exception {

    /**
     * コンストラクタ
     */
    public SkipFileException() {
        super();
    }

    /**
     * コンストラクタ
     *
     * @param msg メッセージ
     */
    public SkipFileException(String msg) {
        super(msg);
    }

    /**
     * コンストラクタ
     *
     * @param cause 原因例外
     */
    public SkipFileException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
