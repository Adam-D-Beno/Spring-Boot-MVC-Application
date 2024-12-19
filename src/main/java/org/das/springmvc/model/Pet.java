package org.das.springmvc.model;

public record Pet(
       Long id,
       String name,
       Long userId
) {
    public boolean isUserIdEmpty() {
        return userId == null;
    }
}
