package ru.nshi.error;

public class SongNotFoundException extends Exception {
    public SongNotFoundException(String message) {
        super(message);
    }
}
