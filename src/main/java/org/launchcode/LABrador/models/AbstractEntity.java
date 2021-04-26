package org.launchcode.LABrador.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class AbstractEntity
{
    @Id
    @GeneratedValue
    private long id;

    public AbstractEntity() {}

    public long getId() {
        return id;
    }
}
