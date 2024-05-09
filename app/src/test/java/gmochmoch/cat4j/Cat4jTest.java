package gmochmoch.cat4j;

import static gmochmoch.cat4j.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link Concatenate}のテスト
 */
class Cat4jTest {

    @DisplayName("ファイル出力のテスト")
    @Nested
    class FileTest {

        @DisplayName("存在しないファイル")
        @Test
        void notfound() throws Exception {
            String notfound = "notfound.txt";
            monitor((out, err) -> {
                assertFalse(new Cat4j().concatenate(notfound), "エラーになること");
                assertEquals("", out.read(), "標準出力には何も表示されないこと");
                assertLinesMatch(Stream.of(notfound + ": No such file or directory"),
                        err.output(), "エラーが表示されること");
            });
        }

        @DisplayName("存在しないファイルが含まれる場合")
        @Test
        void notfound2() throws Exception {
            String notfound = "notfound.txt";
            String input = "This is test.";
            monitor((out, err) -> text(input, file -> {
                assertFalse(new Cat4j().concatenate(file, notfound), "エラーになること");
                assertEquals(input, out.read(), "ファイル内容が読み込めること");
                assertLinesMatch(Stream.of(notfound + ": No such file or directory"),
                        err.output(), "エラーが表示されること");
            }));
            monitor((out, err) -> text(input, file -> {
                assertFalse(new Cat4j().concatenate(notfound, file), "エラーになること");
                assertEquals(input, out.read(), "ファイル内容が読み込めること");
                assertLinesMatch(Stream.of(notfound + ": No such file or directory"),
                        err.output(), "エラーが表示されること");
            }));
        }

        @DisplayName("単一行ファイル(1つ)")
        @Test
        void one_line1() throws Exception {
            String input = "This is test.";
            monitorOut(out -> text(input, file -> {
                assertTrue(new Cat4j().concatenate(file), "正常終了すること");
                assertEquals(input, out.read(), "ファイル内容が読み込めること");
            }));
        }

        @DisplayName("単一行ファイル(2つ)")
        @Test
        void one_line2() throws Exception {
            String i1 = "This is test1.";
            String i2 = "This is test2.";
            monitorOut(out -> text(i1, f1 -> text(i2, f2 -> {
                assertTrue(new Cat4j().concatenate(f1, f2), "正常終了すること");
                assertEquals(i1 + i2, out.read(), "ファイル内容が読み込めること");
            })));
        }

        @DisplayName("複数行ファイル(1つ)")
        @Test
        void multi_line1() throws Exception {
            String input = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった
                    """;
            monitorOut(out -> text(input, file -> {
                assertTrue(new Cat4j().concatenate(file), "正常終了すること");
                assertEquals(input, out.read(), "ファイル内容が読み込めること");
            }));
        }

        @DisplayName("複数行ファイル(3つ)")
        @Test
        void multi_line3() throws Exception {
            String i1 = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった
                    """;
            String i2 = """
                    おばあさんが川で洗濯をしていると
                    大きな桃が
                    　どんぶらこっこ
                    　　どんぶらこっこと
                    """;
            String i3 = """
                    下流に流れていったそうな
                                        
                    おしまい
                    """;
            monitorOut(out -> text(i1, f1 -> text(i2, f2 -> text(i3, f3 -> {
                assertTrue(new Cat4j().concatenate(f1, f2, f3), "正常終了すること");
                assertEquals(i1 + i2 + i3, out.read(), "ファイル内容が読み込めること");
            }))));
        }
    }

    @DisplayName("標準出力のテスト")
    @Nested
    class StandardInputTest {

        @DisplayName("引数なし")
        @Test
        void no_param() throws Exception {
            String input = "This is test.";
            monitorOut(out -> type(input, () -> {
                assertTrue(new Cat4j().concatenate(), "正常終了すること");
                assertEquals(input, out.read(), "引数がない場合は標準出力内容が表示される");
            }));
        }

        @DisplayName("'-' 1つ")
        @Test
        void one_param() throws Exception {
            String input = "This is test.";
            monitorOut(out -> type(input, () -> {
                assertTrue(new Cat4j().concatenate("-"), "正常終了すること");
                assertEquals(input, out.read(), "'-'の場合は標準出力内容が表示される");
            }));
        }

        @DisplayName("'-' 2つ")
        @Test
        void two_params() throws Exception {
            String input = "This is test.";
            monitorOut(out -> type(input, () -> {
                assertTrue(new Cat4j().concatenate("-", "-"), "正常終了すること");
                assertEquals(input, out.read(), "'-'が複数あっても1度だけしか表示されない");
            }));
        }
    }

    @DisplayName("オプションのテスト")
    @Nested
    class OptionTest {

        @DisplayName("存在しないオプション")
        @Test
        void no_such_option() throws Exception {
            monitorOut(out -> {
                assertThrows(RuntimeException.class,
                        () -> assertTrue(new Cat4j().concatenate("--no-such-option")));
                assertEquals("", out.read(), "標準出力には何も表示されないこと");
            });
        }

        @DisplayName("バージョン")
        @Test
        void version() throws Exception {
            monitorOut(out -> {
                assertTrue(new Cat4j().concatenate("--version"));
                assertLinesMatch(Stream.of("Cat4j \\(JDK: .*\\) under construction$"), out.output(),
                        "バージョンが表示される");
            });
            monitorOut(out -> {
                assertTrue(new Cat4j().concatenate("--version", "--help"));
                assertLinesMatch(Stream.of("Cat4j \\(JDK: .*\\) under construction$"), out.output(),
                        "先に指定されたため、バージョンが表示される");
            });
        }

        @DisplayName("ヘルプ")
        @Test
        void help() throws Exception {
            monitorOut(out -> {
                assertTrue(new Cat4j().concatenate("--help"));
                assertLinesMatch(Stream.of("Usage: cat4j [OPTION]... [FILE]...", ">> >>"),
                        out.output(), "ヘルプが表示される");
            });
            monitorOut(out -> {
                assertTrue(new Cat4j().concatenate("--help", "--version"));
                assertLinesMatch(Stream.of("Usage: cat4j [OPTION]... [FILE]...", ">> >>"),
                        out.output(), "先に指定されたためヘルプが表示される");
            });
        }

        @DisplayName("末尾表示")
        @Test
        void show_ends() throws Exception {
            String input = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった
                    """;
            monitorOut(out -> text(input, file -> {
                assertTrue(new Cat4j().concatenate("-E", file), "正常終了すること");
                assertLinesMatch(Stream.of(input.split(Const.LINE_SEPARATOR)).map(l -> l + "\\$"),
                        out.output(), "末尾に $ がつくこと");
            }));
        }

        @DisplayName("タブ表示")
        @Test
        void show_tabs() throws Exception {
            String input = """
                    if now.hour >= 12 and now.hour < 16:
                    \tprint("Hello, world")
                    else:
                    \tprint("Good-by")
                    """;
            String expect = """
                    if now.hour >= 12 and now.hour < 16:
                    ^Iprint("Hello, world")
                    else:
                    ^Iprint("Good-by")
                    """;
            monitorOut(out -> text(input, file -> {
                assertTrue(new Cat4j().concatenate("-T", file), "正常終了すること");
                assertEquals(expect, out.read(), "");
            }));
        }

        @DisplayName("行番号表示")
        @Test
        void number() throws Exception {
            String input = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった
                                        
                    そんなこんなでいろいろあってな
                                        
                                        
                                        
                    おしまい
                    """;
            String expect = """
                    \s    1  昔々あるところにおじいさんとおばあさんがおったそうな
                         2  今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった
                         3 \s
                         4  そんなこんなでいろいろあってな
                         5 \s
                         6 \s
                         7 \s
                         8  おしまい
                    """;
            monitorOut(out -> text(input, file -> {
                assertTrue(new Cat4j().concatenate("-n", file), "正常終了すること");
                assertEquals(expect, out.read(), "行番号がつくこと");
            }));
        }

        @DisplayName("行番号表示(空行除く)")
        @Test
        void number_non_blank() throws Exception {
            String input = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった

                    おしまい
                    """;
            String expect = """
                    \s    1  昔々あるところにおじいさんとおばあさんがおったそうな
                         2  今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった

                         3  おしまい
                    """;
            monitorOut(out -> text(input, file -> {
                assertTrue(new Cat4j().concatenate("-b", file), "正常終了すること");
                assertEquals(expect, out.read(), "行番号がつくこと");
            }));
        }

        @DisplayName("空行をまとめる")
        @Test
        void squeeze_blank() throws Exception {
            String input = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった

                    そんなこんなでいろいろあってな



                    おしまい
                    """;
            String expect = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった

                    そんなこんなでいろいろあってな

                    おしまい
                    """;
            monitorOut(out -> text(input, file -> {
                assertTrue(new Cat4j().concatenate("-s", file), "正常終了すること");
                assertEquals(expect, out.read(), "空行がまとまること");
            }));
        }
    }

}