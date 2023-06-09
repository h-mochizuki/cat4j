package gmochmoch.cat4j;

/**
 * CatのJava実装です
 */
public class Cat4j {

    /**
     * Cat4jを実行します
     *
     * @param args 起動引数
     */
    public static void main(String... args) {
        try {
            if (!new Concatenate().concatenate(args)) {
                // 読み込めないファイルがあったらエラー
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
