package com.iminurnetz.bukkit.plugin.permissions.model.playground;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class LinkDataId implements Serializable {
    private int wordId;
    private int listId;
    
    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int hashCode() {
        return (int) (wordId + listId*10000);
    }
    
    public boolean equals(Object o) {
        if (o instanceof LinkDataId) {
            LinkDataId other = (LinkDataId) o;
            return (other.wordId == wordId) && (other.listId == listId);
        }
        return false;
    }
}
