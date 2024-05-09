package gmochmoch.cat4j;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 単体テストで使う便利メソッド
 */
public class TestHelper {

    /**
     * 標準入力後に指定された処理を実行します
     *
     * @param input    入力文字列
     * @param runnable 実施したい処理
     * @throws Exception 実行時例外
     */
    public static synchronized void type(String input, ThrowableRunnable runnable)
            throws Exception {
        InputStream originIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream(
                    input.getBytes(StandardCharsets.UTF_8)));
            runnable.run();
        } finally {
            System.setIn(originIn);
        }
    }

    /**
     * 一時ファイルに文字列を出力します
     *
     * @param input    ファイルに書き出す文字列
     * @param consumer 一時ファイルパスを引数にした処理
     * @throws Exception 実行時例外
     */
    public static void text(String input, ThrowableConsumer<String> consumer)
            throws Exception {
        final File tmp = getTempFile();
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(tmp)))) {
            writer.write(input);
            writer.flush();
            consumer.accept(tmp.getAbsolutePath());
        } finally {
            if (tmp.exists()) {
                tmp.delete();
            }
        }
    }

    /**
     * 一時ファイルを作成します
     *
     * @return 一時ファイル
     * @throws IOException        ファイル作成例外
     * @throws URISyntaxException ファイルパス取得例外
     */
    private static File getTempFile() throws IOException, URISyntaxException {
        URL url = Objects.requireNonNull(
                TestHelper.class.getResource("."));
        return Files.createTempFile(
                Paths.get(url.toURI()),
                TestHelper.class.getSimpleName(),
                "").toFile();
    }

    /**
     * 標準出力とエラー出力を監視します
     *
     * @param consumer 実行したい処理
     * @throws Exception 実行時例外
     */
    public static synchronized void monitor(ThrowableBiConsumer<SysOut, SysOut> consumer) throws Exception {
        monitorOut(out -> monitorErr(err -> consumer.accept(out, err)));
    }

    /**
     * 標準出力を監視します
     *
     * @param consumer 実行したい処理
     * @throws Exception 実行時例外
     */
    public static synchronized void monitorOut(ThrowableConsumer<SysOut> consumer)
            throws Exception {
        final PrintStream systemOut = System.out;
        try (SysOut out = new SysOut()) {
            System.setOut(out);
            consumer.accept(out);
        } finally {
            System.setOut(systemOut);
        }
    }

    /**
     * エラー出力を監視します
     *
     * @param consumer 実行したい処理
     * @throws Exception 実行時例外
     */
    public static synchronized void monitorErr(ThrowableConsumer<SysOut> consumer)
            throws Exception {
        final PrintStream systemErr = System.err;
        try (SysOut err = new SysOut()) {
            System.setErr(err);
            consumer.accept(err);
        } finally {
            System.setErr(systemErr);
        }
    }

    /**
     * {@link PrintStream} のラッパークラス
     */
    public static class SysOut extends PrintStream {

        /**
         * 出力内容保持のストリーム
         */
        private final ByteArrayOutputStream os;

        /**
         * コンストラクタ
         */
        private SysOut() {
            this(new ByteArrayOutputStream());
        }

        /**
         * コンストラクタ
         *
         * @param os 出力内容保持のストリーム
         */
        private SysOut(ByteArrayOutputStream os) {
            super(new BufferedOutputStream(os));
            this.os = os;
        }

        /**
         * 出力内容を取得します
         *
         * @return 出力内容
         */
        public String read() {
            flush();
            return os.toString();
        }

        /**
         * 出力内容を取得します
         *
         * @return 出力内容の {@link Stream}
         */
        public Stream<String> output() {
            return Stream.of(read().split("[\r\n]"));
        }
    }

    /**
     * 例外を投げられる{@link Runnable}インターフェース
     */
    @FunctionalInterface
    public interface ThrowableRunnable {

        /**
         * 処理を実行します
         *
         * @throws Exception 実行時に発生した例外
         */
        void run() throws Exception;
    }

    /**
     * 例外を投げられる{@link java.util.function.Consumer}インターフェース
     *
     * @param <T> 引数クラス
     */
    @FunctionalInterface
    public interface ThrowableConsumer<T> {

        /**
         * 処理を実行します
         *
         * @param t 引数
         * @throws Exception 実行時例外
         */
        void accept(T t) throws Exception;
    }

    /**
     * 例外を投げられる{@link java.util.function.BiConsumer}インターフェース
     *
     * @param <T> 引数クラス
     * @param <U> 引数クラス
     */
    @FunctionalInterface
    public interface ThrowableBiConsumer<T, U> {

        /**
         * 処理を実行します
         *
         * @param t 引数
         * @param u 引数
         * @throws Exception 実行時例外
         */
        void accept(T t, U u) throws Exception;
    }
}
