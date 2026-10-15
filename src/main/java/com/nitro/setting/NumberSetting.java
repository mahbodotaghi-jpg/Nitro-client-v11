package com.nitro.setting;

public class NumberSetting {
    public final String name;
    public final double min, max, step;
    private double value;

    public NumberSetting(String name, double def, double min, double max, double step) {
        this.name = name; this.value = def; this.min = min; this.max = max; this.step = step;
    }
    public double get() { return value; }
    public int getInt() { return (int) Math.round(value); }
    public void set(double v) {
        v = Math.max(min, Math.min(max, v));
        value = Math.round(v / step) * step;
    }
}
