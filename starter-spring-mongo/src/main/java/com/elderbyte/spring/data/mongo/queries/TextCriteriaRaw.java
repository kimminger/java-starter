package com.elderbyte.spring.data.mongo.queries;

import org.bson.Document;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public class TextCriteriaRaw extends TextCriteria {

    private final String rawSearch;

    private @Nullable String language;
    private @Nullable Boolean caseSensitive;
    private @Nullable Boolean diacriticSensitive;


    protected TextCriteriaRaw(String rawSearch) {
        this(rawSearch, null);
    }

    protected TextCriteriaRaw(String rawSearch, @Nullable String language) {

        this.rawSearch = rawSearch;
        this.language = language;
    }

    /**
     * Returns a new {@link TextCriteria} for the default language.
     *
     * @return
     */
    public static TextCriteriaRaw search(String rawQuery) {
        return new TextCriteriaRaw(rawQuery, null);
    }

    /**
     * For a full list of supported languages see the mongodb reference manual for
     * <a href="https://docs.mongodb.org/manual/reference/text-search-languages/">Text Search Languages</a>.
     *
     * @param language
     * @return
     */
    public static TextCriteriaRaw search(String rawQuery, String language) {
        return new TextCriteriaRaw(rawQuery, language);
    }

    /**
     * Optionally enable or disable case sensitive search.
     *
     * @param caseSensitive boolean flag to enable/disable.
     * @return never {@literal null}.
     * @since 1.10
     */
    @Override
    public TextCriteriaRaw caseSensitive(boolean caseSensitive) {

        this.caseSensitive = caseSensitive;
        return this;
    }

    /**
     * Optionally enable or disable diacritic sensitive search against version 3 text indexes.
     *
     * @param diacriticSensitive boolean flag to enable/disable.
     * @return never {@literal null}.
     * @since 1.10
     */
    @Override
    public TextCriteriaRaw diacriticSensitive(boolean diacriticSensitive) {

        this.diacriticSensitive = diacriticSensitive;
        return this;
    }



    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.core.query.CriteriaDefinition#getCriteriaObject()
     */
    @Override
    public Document getCriteriaObject() {

        Document document = new Document();

        if (StringUtils.hasText(language)) {
            document.put("$language", language);
        }

        document.put("$search", rawSearch);

        if (caseSensitive != null) {
            document.put("$caseSensitive", caseSensitive);
        }

        if (diacriticSensitive != null) {
            document.put("$diacriticSensitive", diacriticSensitive);
        }

        return new Document(getKey(), document);
    }
}