package net.veniture.exercise.jira.settings;

public interface AppSettings {

    //Exercise key component flag; DO NOT USE
    String DEFAULT_BOOKS_CREATED = "net.veniture.jira.exercise.default-books-created";

	//Plugin settings keys
	String BOOK_LIST_PAGINATION_ENABLED = "net.veniture.jira.settings.book-list-pagination";
    String BOOK_LIST_PAGE_ITEM_COUNT = "net.veniture.jira.settings.book-list-count";
    String BOOK_LIST_ANNOUNCEMENT = "net.veniture.jira.settings.book-list-announcement";

    Boolean getBoolean(String key);
    void setBoolean(String key, Boolean value);

    String getString(String key);
    void setString(String key, String value);

    Integer getInteger(String key);
    void setInteger(String key, Integer value);


}
