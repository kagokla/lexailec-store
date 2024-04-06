package com.github.kagokla.store.model;

import com.github.kagokla.store.model.utils.ValidatorUtils;
import lombok.Getter;

@Getter
public abstract class BaseEntity {

    protected final String id;

    protected BaseEntity(final String id) {
        ValidatorUtils.requireNonBlank(id, "entity's id");

        this.id = id;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final var other = (BaseEntity) o;
        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
