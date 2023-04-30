package gmochmoch.cat4j;

import gmochmoch.cat4j.exception.CancelException;
import gmochmoch.cat4j.exception.SkipFileException;
import gmochmoch.cat4j.option.OptionHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static gmochmoch.cat4j.Const.STD_INPUT_PARAM;

/**
 * 引数で指定されたファイルもしくは標準入力で渡された内容を標準出力で表示します
 */
public class Concatenate {

    // バッファサイズ
    private static final int BUFFER_SIZE = 4 * 1024;
    // 入力ストリームを再読み込みしないためのフラグ
    private boolean inputStreamClosed = false;

    /**
     * 引数を解析し、指定されたファイル内容もしくは標準入力内容を標準出力で表示します
     *
     * @param args 引数
     * @return true:全てのファイルが正常に出力された<br/>false:出力の際に問題があった
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
                System.out.print(handler.convert(new String(buffer, 0, size)));
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
        if (Objects.isNull(arg) || arg.isBlank() || STD_INPUT_PARAM.equals(arg)) {
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
            throw new SkipFileException();
        }
        inputStreamClosed = true;
        return new InputStreamReader(System.in);
    }
}
