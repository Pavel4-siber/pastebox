package ru.example.pastebox.exception;

/**
 * @author Zhurenkov Pavel 13.08.2023
 */

public class NotFoundPasteboxException extends RuntimeException {
    public NotFoundPasteboxException(String s) {
        super(s);
    }
}
