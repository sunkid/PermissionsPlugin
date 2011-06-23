package com.iminurnetz.bukkit.plugin.permissions.model.playground;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Version;
import javax.persistence.Table;
import javax.persistence.EmbeddedId;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;


@Entity
@Table(name = "link_data")
public class LinkData {
    @EmbeddedId
    private LinkDataId key;

    private int rankOrder;
    
    @Version
    private int version;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "list_id", insertable = false, updatable = false)
    private ListData list;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "word_id", insertable = false, updatable = false)
    private StringData word;

    public LinkData() {
        key = new LinkDataId();
    }
    
    public int getRankOrder() {
        return rankOrder;
    }

    public void setRankOrder(int rankOrder) {
        this.rankOrder = rankOrder;
    }

    public void setWord(StringData word) {
        this.word = word;
    }

    public StringData getWord() {
        return word;
    }

    public void setList(ListData list) {
        this.list = list;
    }

    public ListData getList() {
        return list;
    }

    public void setKey(LinkDataId key) {
        this.key = key;
    }

    public LinkDataId getKey() {
        return key;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
