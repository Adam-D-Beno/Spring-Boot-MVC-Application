package org.das.springmvc.model;

//todo export in main branch
public record Pet(
       Long id,
       String name,
       Long userId
) {
    public boolean isUserIdEmpty() {
        return userId == null;
    }
}
