package com.npd.npdstreaming.model;

public enum Category {
    ACAO("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime"),
    AVENTURA("Adventure", "Aventura"),
    ANIMACAO("Animation", "Animação");

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
