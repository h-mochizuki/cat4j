package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.Cat4j;
import gmochmoch.cat4j.exception.CancelException;
import gmochmoch.cat4j.option.IOption;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * バージョンを表示します
 */
public class VersionOption implements IOption {

    // バージョンフォーマット
    private static final String VERSION_FORM = "%s (build: %s)";
    // 日付用フォーマット
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // パッケージ分割子
    private static final String PACKAGE_SEPARATOR = ".";
    // パス分割子
    private static final String PATH_SEPARATOR = "/";
    // クラス拡張子
    private static final String CLASS_EXTENSION = ".class";

    /**
     * バージョンを取得して処理をキャンセルさせます
     */
    @Override
    public void init() {
        throw new CancelException(
                String.format(VERSION_FORM, Cat4j.class.getSimpleName(), getVersion()));
    }

    /**
     * バージョンを返します
     *
     * @return バージョン
     */
    private String getVersion() {
        try {
            // バージョンとして Cat4j がコンパイルされた日付を返すことにしました
            String path = Cat4j.class.getCanonicalName().replace(PACKAGE_SEPARATOR, PATH_SEPARATOR) + CLASS_EXTENSION;
            URI uri = Objects.requireNonNull(getClass().getClassLoader().getResource(path)).toURI();
            return DATE_FORMAT.format(new Date(new File(uri).lastModified()));
        } catch (Exception ignore) {
            // 取得できない場合は現在日付
            return DATE_FORMAT.format(new Date());
        }
    }
}
