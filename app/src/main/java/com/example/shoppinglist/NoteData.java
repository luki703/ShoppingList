package com.example.shoppinglist;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class NoteData implements  Serializable {
    private String content;
    private Date data;

    public NoteData(String content, Date data) {
        this.content = content;
        this.data = data;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
