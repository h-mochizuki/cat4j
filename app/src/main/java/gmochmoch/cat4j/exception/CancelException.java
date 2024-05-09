package gmochmoch.cat4j.exception;

/**
 * 処理をキャンセルする例外です
 * <p/>
 * この例外のメッセージを表示した後で{@code System.exit(0)}を実行します
 */
public class CancelException extends RuntimeException {

    /**
     * コンストラクタ
     *
     * @param msg 表示メッセージ
     */
    public CancelException(String msg) {
        super(msg);
    }
}
