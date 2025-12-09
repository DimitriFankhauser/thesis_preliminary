package org.acme;

// This class needs to be defined in your org.acme package

public class EncryptableMessage {

    public int id;
    public String message;

    public EncryptableMessage() {
    }

    public EncryptableMessage(int id, String message) {
        this.id = id;
        this.message = message;
    }
}