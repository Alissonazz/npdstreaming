package com.npd.npdstreaming.model;

public enum Category {
    ACTION("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDY("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime"),
    ADVENTURE("Adventure", "Aventura"),
    ANIMATION("Animation", "Animação"),
    BIOGRAPHY("Biography", "Biografia;"),
    TERROR("Horror", "Terror");

    private String categoryOmdb;
    private String categoryOmdbPtBr;

    Category(String categoryOmdb, String categoryOmdbPtBr){
        this.categoryOmdb = categoryOmdb;
        this.categoryOmdbPtBr = categoryOmdbPtBr;
    }

    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.categoryOmdb.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }

    public static Category fromStringPt(String text) {
        for (Category category : Category.values()) {
            if (category.categoryOmdbPtBr.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
