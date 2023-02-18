package ru.javawebinar.webapp.model;

public enum Contacts {
    TELEPHONE("Телефон"),
    SKYPE("Skype") {
        @Override
        public String toHtml0(String value) {
            return "Skype: " + "<a href='skype:" + value + "'>" + value + "</a>";
        }
    },
    MAIL("Почта") {
        @Override
        public String toHtml0(String value) {
            return "Почта: " + "<a href='mailto:" + value + "'>" + value + "</a>";
        }
    },
    LINKEDIN("Профиль LinkedIn") {
//        @Override
//        public String toHtml0(String value) {
//            return "<a href='mailto:" + value + "'>" + getTitle() + "</a>";
//        }
    },
    GITHUB("Профиль GitHub"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMEPAGE("Сайт");

    private final String title;

    Contacts(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    protected String toHtml0(String value) {
        return "<a href=" + value + ">" + title + "</a>";
    }

    public String toHtml(String value) {
        return (value == null) ? "" : toHtml0(value);
    }
}
