package net.veniture.exercise.jira.entity;

import net.java.ao.Entity;

public interface Book extends Entity {
    String getTitle();
    void setTitle(String title);

    String getAuthor();
    void setAuthor(String author);
}
