package com.soybean.utils;

public class SpyglassRenderState {
    private static final ThreadLocal<Boolean> IS_HANDHELD = ThreadLocal.withInitial(() -> false);

    public static void setHandheld(boolean value) {
        IS_HANDHELD.set(value);
    }

    public static boolean isHandheld() {
        return IS_HANDHELD.get();
    }

    public static void clear() {
        IS_HANDHELD.remove();
    }
}
