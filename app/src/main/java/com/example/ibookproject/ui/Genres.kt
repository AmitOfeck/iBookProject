package com.example.ibookproject.ui

enum class Genres(val displayName: String) {
    FICTION("Fiction"),
    NON_FICTION("Non-Fiction"),
    FANTASY("Fantasy"),
    SCI_FI("Science Fiction"),
    MYSTERY("Mystery"),
    ROMANCE("Romance"),
    THRILLER("Thriller"),
    BIOGRAPHY("Biography"),
    HISTORY("History");

    companion object {
        fun getAll() = entries.map { it.displayName }
    }
}