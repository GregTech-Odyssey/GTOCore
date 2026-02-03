package com.gtocore.integration.ae.hooks;

public interface IMouseNoRedirection {

    default boolean gtocore$shouldRedirectMouse() {
        return false;
    }

    default void setRedirectMouse(boolean redirect) {}
}
