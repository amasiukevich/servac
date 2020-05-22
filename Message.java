package ClientStuff;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {

    private int userId;
    private String text;
    private int messSize;
    private Date sentDate;
    static final int maxLength = 140;

    private String reduceMess(String original) {

        System.out.println("In the reduce mess");
        char[] temp_mess = original.toCharArray();
        char[] buff = new char[maxLength];
        for (int i = 0; i < maxLength; i++) {
            buff[i] = temp_mess[i];
        }
        String modified = new String(buff);
        System.out.println(modified.length());
        return modified;

    }


    public Message(int userId, String text) {

        if (text.length() < maxLength) {
            this.messSize = text.length();
            this.text = text;
        } else {
            this.text = reduceMess(text);
            this.messSize = maxLength;
        }

        this.userId = userId;
        Date now = new Date();
        this.sentDate = now;
    }

    // in case we want to edit the message
    void modify(String text) {
        if (text.length() < maxLength) {
            this.text = text;
            this.messSize = text.length();
        } else {
            text = reduceMess(text);
            this.messSize = 140;
        }
    }

    public int getSize() {
        return messSize;
    }

    public String getText() {
        return text;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public int getUserId() {
        return userId;
    }
}
