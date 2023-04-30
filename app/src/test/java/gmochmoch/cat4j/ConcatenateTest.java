package gmochmoch.cat4j;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.Stream;

import static gmochmoch.cat4j.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

class ConcatenateTest {

    @Nested
    class OptionTest {
        @Test
        void concatenate() throws Exception {
            monitor(out -> {
                assertTrue(new Concatenate().concatenate("--version"));
                assertLinesMatch(Stream.of("Cat4j \\(build: .*\\)$"), out.output(), "バージョンが表示される");
            });
            monitor(out -> {
                assertTrue(new Concatenate().concatenate("example.txt", "--version"));
                assertLinesMatch(Stream.of("Cat4j \\(build: .*\\)$"), out.output(), "バージョン表示が優先される");
            });
        }
    }

    @Nested
    class FileTest {
        @Test
        void concatenate() throws Exception {
            String input = lfc("""
                    This is test
                    Random int: %d""".formatted(new Random().nextInt()));
            monitor(out -> text(input, file -> {
                assertTrue(new Concatenate().concatenate(file));
                assertEquals(input, out.read(), "ファイル内容が読み込めること");
            }));
            String i1 = "testA";
            String i2 = "testB";
            String i3 = "testC";
            monitor(out -> text(i1, f1 -> type(i2, () -> text(i3, f3 -> {
                assertTrue(new Concatenate().concatenate("-n", f1 , "-", f3));
                assertEquals("     1  " + i1 + i2 + i3, out.read());
            }))));
        }
    }

    @Nested
    class StandardInputTest {

        @Test
        void concatenate() throws Exception {
            // 出力内容は毎回変える
            final String input = getClass().getName() + new Random().nextInt();

            monitor(out -> type(input, () -> {
                assertTrue(new Concatenate().concatenate());
                assertEquals(input, out.read(), "引数がない場合は標準出力内容が表示される");
            }));
            monitor(out -> type(input, () -> {
                assertTrue(new Concatenate().concatenate("-"));
                assertEquals(input, out.read(), "'-'の場合は標準出力内容が表示される");
            }));
            monitor(out -> type(input, () -> {
                assertTrue(new Concatenate().concatenate("-", "-"));
                assertEquals(input, out.read(), "'-'が複数あっても1度だけしか表示されない");
            }));
            monitor(out -> type(input, () -> {
                assertTrue(new Concatenate().concatenate("-E", "-"));
                assertEquals(input, out.read(), "'-'が複数あっても1度だけしか表示されない");
            }));
        }
    }
}