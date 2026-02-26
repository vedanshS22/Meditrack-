package com.airtribe.meditrack.entity;

import java.util.Objects;

/**
 * SOLID (Abstraction): common base type for all core medical domain entities
 * so that shared identity and equality logic is defined once.
 */
public abstract class MedicalEntity {

    private final String id;

    protected MedicalEntity(String id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicalEntity that = (MedicalEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

