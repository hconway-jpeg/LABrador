package org.launchcode.LABrador.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

public class AbstractEntity
{
    @Id
    @GeneratedValue
    private long id;

    public AbstractEntity() {}

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity entity = (AbstractEntity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
