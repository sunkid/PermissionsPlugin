package com.iminurnetz.bukkit.plugin.permissions.model.playground;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity  
@Table(name="string_data")  
public class StringData {
    @Id  
    private int wordId;
    private String word;
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy="word")
    private List<LinkData> list;
 
    public StringData() {      
    }
    
    public StringData(int id, String word) {
        this.wordId = id;
        this.word = word;
        list = new ArrayList<LinkData>();
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int id) {
        this.wordId = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setList(List<LinkData> list) {
        this.list = list;
    }

    public List<LinkData> getList() {
        return list;
    }
}
