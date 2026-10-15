package com.nitro.module;

public enum Category {
    COMBAT("Combat", "C"),
    RENDER("Render", "R"),
    MISC("Misc", "M"),
    DONUT("Donut", "D");

    public final String label, icon;
    Category(String label, String icon) { this.label = label; this.icon = icon; }
}
