package gmochmoch.cat4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * cat コマンドを Java で再現したクラスです
 */
public class Cat4j {

    // ストリーム読み込み用のバッファサイズ
    private static final int BUFFER_SIZE = 4 * 1024;
    // 標準入力を読み込むためのフラグ
    private static final String HYPHEN = "-";
    // 標準入力読み込みフラグ
    private boolean stdinLoad = false;

    /**
     * Cat4jを実行します
     *
     * @param args 起動引数
     */
    public static void main(String[] args) {
        Cat4j app = new Cat4j();
        for (String arg : args) {
            app.concatenate(arg);
        }
    }

    /**
     * 入力された引数に対応した内容を表示します
     *
     * @param arg 引数
     */
    public void concatenate(String arg) {
        try (BufferedReader reader = inputStreamReader(arg)) {
            if (reader == null) {
                return;
            }
            // 改行コード関係なくバッファ分だけ文字列を取得する
            char[] buffer = new char[BUFFER_SIZE];
            for (int size; (size = reader.read(buffer)) != -1; ) {
                String text = new String(buffer, 0, size);
                System.out.print(text);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * 引数に応じた入力ストリームを返します
     *
     * @param arg 引数
     * @return 入力ストリーム
     * @throws IOException 入力例外
     */
    private BufferedReader inputStreamReader(String arg) throws IOException {
        if (arg == null || arg.isBlank() || HYPHEN.equals(arg)) {
            if (stdinLoad) {
                // 既に読み込んでいたらnullを返す
                return null;
            } else {
                stdinLoad = true;
                return new BufferedReader(new InputStreamReader(System.in));
            }
        } else {
            return fileReader(arg);
        }
    }

    private BufferedReader fileReader(String arg) throws IOException {
        try {
            return new BufferedReader(new InputStreamReader(
                    new FileInputStream(arg), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            System.err.println("cat: " + arg + ": No such file or directory");
            return null;
        }
    }
}
