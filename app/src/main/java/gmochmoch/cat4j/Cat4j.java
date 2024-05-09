package gmochmoch.cat4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import gmochmoch.cat4j.exception.CancelException;
import gmochmoch.cat4j.exception.SkipFileException;
import gmochmoch.cat4j.option.OptionHandler;

/**
 * cat コマンドを Java で再現したクラスです
 */
public class Cat4j {

    // ストリーム読み込み用のバッファサイズ
    private static final int BUFFER_SIZE = 4 * 1024;
    // 標準入力読み込みフラグ
    private boolean inputStreamClosed = false;

    /**
     * Cat4jを実行します
     *
     * @param args 起動引数
     */
    public static void main(String[] args) {
        try {
            if (!new Cat4j().concatenate(args)) {
                // 読み込めないファイルがあったらエラー
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * 入力された引数に対応した内容を表示します
     *
     * @param arg 引数
     */
    public boolean concatenate(String... args) {
        try {
            final OptionHandler handler = new OptionHandler();
            handler.init(args);
            inputStreamClosed = false;
            boolean success = true;
            for (String arg : handler.getRemains()) {
                success &= concatenate(arg, handler);
            }
            return success;
        } catch (CancelException e) {
            System.out.println(e.getMessage());
            return true;
        }
    }

    /**
     * 指定されたひとつのファイル、もしくは標準入力の内容を標準出力で表示します
     *
     * @param arg     ファイルもしくは標準入力フラグ
     * @param handler オプションを管理するハンドラー
     * @return true:正常に出力された<br/>false:出力の際に問題があった
     */
    private boolean concatenate(String arg, OptionHandler handler) {
        try (BufferedReader reader = new BufferedReader(getInputStreamReader(arg))) {
            char[] buffer = new char[BUFFER_SIZE];
            for (int size; (size = reader.read(buffer)) != -1; ) {
                System.out.print(handler.transform(new String(buffer, 0, size)));
            }
            return true;
        } catch (SkipFileException | IOException e) {
            String msg = e.getMessage();
            if ((Objects.isNull(msg) || msg.isBlank()) && inputStreamClosed) {
                // 標準出力の再読み込みは成功とみなす
                return true;
            } else {
                // それ以外の場合はエラー出力して失敗とする
                System.err.println(e.getMessage());
                return false;
            }
        }
    }

    /**
     * 引数の内容を判別し、標準入力ストリームもしくはファイル入力ストリームを返します
     *
     * @param arg 引数
     * @return 入力ストリーム
     * @throws SkipFileException ファイル読込みが失敗した場合
     */
    private InputStreamReader getInputStreamReader(String arg)
            throws SkipFileException {
        if (Objects.isNull(arg) || arg.isBlank() || Const.STD_INPUT_PARAM.equals(arg)) {
            return getStandardInputStream();
        } else {
            return getFileInputStream(arg);
        }
    }

    /**
     * ファイルパスから対応するファイル入力ストリームを返します
     *
     * @param filePath ファイルパス
     * @return ファイル入力ストリーム
     * @throws SkipFileException ファイル読込みが失敗した場合
     */
    private InputStreamReader getFileInputStream(String filePath)
            throws SkipFileException {
        return getFileInputStream(filePath, StandardCharsets.UTF_8.name());
    }

    /**
     * ファイルパスから対応するファイル入力ストリームを返します
     *
     * @param filePath ファイルパス
     * @param encoding 文字コード
     * @return ファイル入力ストリーム
     * @throws SkipFileException ファイル読込みが失敗した場合
     */
    InputStreamReader getFileInputStream(String filePath, String encoding) throws SkipFileException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new SkipFileException(file.getPath() + ": No such file or directory");
        }
        if (file.isDirectory()) {
            throw new SkipFileException(file.getPath() + ": Is a directory");
        }
        try {
            return new InputStreamReader(new FileInputStream(file), encoding);
        } catch (IOException e) {
            throw new SkipFileException(e);
        }
    }

    /**
     * 標準入力から読み込む入力ストリームを返します
     *
     * @return 標準入力ストリーム
     * @throws SkipFileException ストリームが閉じている場合
     */
    private InputStreamReader getStandardInputStream() throws SkipFileException {
        if (inputStreamClosed) {
        	// 既にストリームが閉じている(一回読み込んでいる)
            throw new SkipFileException();
        }
        inputStreamClosed = true;
        return new InputStreamReader(System.in);
    }
}
