package com.prestige.models;

public class LessonTemplate {
    private Long id;
    private String templateName;
    private String description;

    public LessonTemplate() {}

    public LessonTemplate(Long id, String templateName) {
        this.id = id;
        this.templateName = templateName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "LessonTemplate{" +
                "id=" + id +
                ", templateName='" + templateName + '\'' +
                '}';
    }
}