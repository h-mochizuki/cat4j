package gmochmoch.cat4j.common;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * MANIFEST.MFから情報を取得するクラスです
 * <p>
 * このクラスはMANIFEST.MFの次の要素を参照します
 * <dl>
 * <dt>Implementation-Version</dt><dd>バージョン</dd>
 * <dt>Build-Timestamp</dt><dd>ビルド日時</dd>
 * </dl>
 */
public class ManifestUtil {

    // 日付フォーマット
    private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    /**
     * このクラスが含まれるjarを探索し、{@link Manifest MANIFEST.MF 情報}を返します。
     * MANIFEST.MFが見つからない場合は null を返します。
     * 
     * @return MANIFEST.MF 情報
     */
    private static Manifest getManifest() {
        String className = ManifestUtil.class.getName();
        String classPath = className.replaceAll("\\.", "/") + ".class";
        URL url = ManifestUtil.class.getClassLoader().getResource(classPath);
        if (url !=null && "jar".equals(url.getProtocol())) {
            String jarPath = url.getPath().split("!")[0];
            jarPath = jarPath.startsWith("file:") ? jarPath.replaceFirst("file:", "") : jarPath;
            try(JarFile jar = new JarFile(jarPath)) {
                return jar.getManifest();
            } catch (IOException notFound) {
                // ignore
            }
        }
        return null;
    }

    /**
     * {@link ManifestUtil MANIFEST.MF}から指定された属性を取得します
     * 
     * @param name 属性名
     * @param defaultValue 属性が取得できなかった場合の値
     * @return 属性の値
     */
    private static String getAttribute(String name, String defaultValue) {
        Manifest manifest = getManifest();
        String value = manifest == null
            ? null
            : manifest.getMainAttributes().getValue(name);
        return value == null ? defaultValue : value;
    }

    /**
     * {@link ManifestUtil MANIFEST.MF}に記述された{@code Implementation-Version}より、jarのバージョンを返します
     * <p>
     * ビルドされていない場合は{@code under construction}(工事中)を返します
     * 
     * @return jarのバージョン
     */
    public static String version() {
        return getAttribute("Implementation-Version", "under construction");
    }

    /**
     * {@link ManifestUtil MANIFEST.MF}に記述された{@code Build-Timestamp}より、jarのビルド日時を返します
     * <p>
     * ビルドされていない場合は現在日時を返します
     * 
     * @return jarのビルド日時
     */
    public static String builtAt() {
        return getAttribute("Build-Timestamp", DF.format(new Date()));
    }
}
