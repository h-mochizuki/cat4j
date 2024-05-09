package gmochmoch.cat4j.option.impl;

import gmochmoch.cat4j.Cat4j;
import gmochmoch.cat4j.common.ManifestUtil;
import gmochmoch.cat4j.exception.CancelException;
import gmochmoch.cat4j.option.IOption;
import gmochmoch.cat4j.option.OptionContext;

/**
 * バージョンを表示します
 */
public class VersionOption implements IOption {

    // バージョンフォーマット
    private static final String VERSION_FORM = "%s (JDK: %s) %s";

    /**
     * バージョンを取得して処理をキャンセルさせます
     */
    @Override
    public void init(OptionContext context) {
        throw new CancelException(
                String.format(VERSION_FORM, Cat4j.class.getSimpleName(), ManifestUtil.buildJdk(), ManifestUtil.version()));
    }
}
