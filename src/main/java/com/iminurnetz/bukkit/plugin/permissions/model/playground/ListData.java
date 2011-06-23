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
@Table(name="list_data")  
public class ListData {
    @Id  
    private int listId;
    private String name;
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy="list")
    private List<LinkData> words;

    public ListData() {
        words = new ArrayList<LinkData>();        
    }
    
    public ListData(int id) {
        this();
        this.listId = id;
    }

    public List<LinkData> getWords() {
        return words;
    }

    public void setWords(List<LinkData> words) {
        this.words = words;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int id) {
        this.listId = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void addWord(StringData word, int rankOrder) {
        LinkData link = new LinkData();
        link.setWord(word);
        link.setList(this);
        link.getKey().setWordId(word.getWordId());
        link.getKey().setListId(this.getListId());
        link.setRankOrder(rankOrder);
        
        this.words.add(link);
        word.getList().add(link);
    }
}
