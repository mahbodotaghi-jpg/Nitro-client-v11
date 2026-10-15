package com.nitro.gui;

import com.nitro.NitroClient;
import com.nitro.module.Category;
import com.nitro.module.Module;
import com.nitro.setting.NumberSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class ClickGui extends Screen {
    private static final int K_BG = 0xF0121218, K_PANEL = 0xFF1A1A22, K_ACCENT = 0xFFF5B82E,
            K_TEXT = 0xFFFFFFFF, K_DIM = 0xFF8A8A99, K_ROW_SEL = 0xFF3A3020, K_OFF = 0xFF2A2A34;
    // Faded copies of the colours above, refreshed every frame for the open animation.
    private int BG, PANEL, ACCENT, TEXT, DIM, ROW_SEL, OFF;
    private static final float OPEN_MS = 220f;
    private final long openedAt = System.currentTimeMillis();
    private static final int W = 420, H = 240, TAB_W = 30, LIST_W = 120;

    private int x = -1, y = -1;
    private Category cat = Category.COMBAT;
    private Module selected;
    private boolean dragging, binding;
    private double dragDX, dragDY;
    private NumberSetting draggedSlider;
    private int scroll;

    public ClickGui() { super(Text.literal("Nitro")); }

    @Override public boolean shouldPause() { return false; }

    private static int fade(int argb, float f) {
        int a = Math.max(5, (int) ((argb >>> 24) * f)); // text renderer treats alpha < 4 as opaque
        return (a << 24) | (argb & 0xFFFFFF);
    }

    private List<Module> list() { return NitroClient.modules.in(cat); }

    @Override public void render(DrawContext c, int mx, int my, float delta) {
        if (x < 0) { x = (width - W) / 2; y = (height - H) / 2; }

        // Open animation: fade in, ease-out scale from 92% and a small slide up.
        float tt = Math.min(1f, (System.currentTimeMillis() - openedAt) / OPEN_MS);
        float ease = 1f - (float) Math.pow(1f - tt, 3);
        BG = fade(K_BG, ease); PANEL = fade(K_PANEL, ease); ACCENT = fade(K_ACCENT, ease);
        TEXT = fade(K_TEXT, ease); DIM = fade(K_DIM, ease); ROW_SEL = fade(K_ROW_SEL, ease); OFF = fade(K_OFF, ease);
        c.fill(0, 0, width, height, fade(0x88000000, ease));
        var ms = c.getMatrices();
        ms.push();
        float cx = x + W / 2f, cy = y + H / 2f, scl = 0.92f + 0.08f * ease;
        ms.translate(cx, cy + (1f - ease) * 12f, 0f);
        ms.scale(scl, scl, 1f);
        ms.translate(-cx, -cy, 0f);

        c.fill(x, y, x + W, y + H, BG);
        c.fill(x, y, x + W, y + 18, PANEL);
        c.drawText(textRenderer, "NITRO", x + 8, y + 5, ACCENT, false);
        long on = NitroClient.modules.modules.stream().filter(Module::isEnabled).count();
        String en = on + " enabled";
        c.drawText(textRenderer, en, x + W - 8 - textRenderer.getWidth(en), y + 5, DIM, false);

        int ty = y + 26;
        for (Category k : Category.values()) {
            boolean s = k == cat;
            c.fill(x + 2, ty, x + TAB_W, ty + 26, s ? ROW_SEL : PANEL);
            if (s) c.fill(x + 2, ty, x + 4, ty + 26, ACCENT);
            c.drawText(textRenderer, k.icon, x + 14, ty + 9, s ? ACCENT : DIM, false);
            ty += 30;
        }

        int lx = x + TAB_W + 6, ly = y + 26;
        c.drawText(textRenderer, cat.label, lx, ly - 2, ACCENT, false);
        int top = y + 40, bottom = y + H - 6;
        scroll = Math.max(0, Math.min(scroll, Math.max(0, list().size() * 24 - (bottom - top))));
        ly = top - scroll;
        c.enableScissor(lx, top, lx + LIST_W, bottom);
        for (Module m : list()) {
            boolean s = m == selected;
            c.fill(lx, ly, lx + LIST_W, ly + 20, s ? ROW_SEL : PANEL);
            c.drawText(textRenderer, m.name, lx + 6, ly + 6, TEXT, false);
            int sx = lx + LIST_W - 26;
            c.fill(sx, ly + 6, sx + 20, ly + 14, OFF);
            int kx = m.isEnabled() ? sx + 10 : sx;
            c.fill(kx, ly + 6, kx + 10, ly + 14, m.isEnabled() ? ACCENT : DIM);
            ly += 24;
        }
        c.disableScissor();

        int px = lx + LIST_W + 10, pw = x + W - 8 - px;
        if (selected != null) {
            c.drawText(textRenderer, selected.name, px, y + 28, TEXT, false);
            c.drawText(textRenderer, selected.description, px, y + 40, DIM, false);
            c.fill(px, y + 54, px + pw, y + 72, PANEL);
            String st = selected.isEnabled() ? "Enabled" : "Disabled";
            c.drawText(textRenderer, st, px + pw / 2 - textRenderer.getWidth(st) / 2, y + 59, DIM, false);

            int sy = y + 84;
            for (NumberSetting n : selected.settings) {
                c.drawText(textRenderer, n.name, px, sy, TEXT, false);
                String v = n.step < 1 ? String.format("%.2f", n.get()) : String.valueOf(n.getInt());
                c.drawText(textRenderer, v, px + pw - textRenderer.getWidth(v), sy, TEXT, false);
                c.fill(px, sy + 14, px + pw, sy + 17, OFF);
                int fw = (int) (pw * (n.get() - n.min) / (n.max - n.min));
                c.fill(px, sy + 14, px + fw, sy + 17, ACCENT);
                sy += 28;
            }
            c.fill(px, y + H - 34, px + pw, y + H - 18, PANEL);
            String bind = binding ? "Press a key..." : "Bind: " + (selected.key > 0 ? keyName(selected.key) : "none");
            c.drawText(textRenderer, bind, px + pw / 2 - textRenderer.getWidth(bind) / 2, y + H - 30, DIM, false);
        }
        c.drawText(textRenderer, "RShift close \u00b7 RMB toggle \u00b7 Drag top strip", x + 4, y + H + 4, DIM, false);
        ms.pop();
    }

    private String keyName(int k) {
        String n = GLFW.glfwGetKeyName(k, 0);
        return n != null ? n.toUpperCase() : "KEY " + k;
    }

    @Override public boolean mouseClicked(double mx, double my, int btn) {
        if (my >= y && my < y + 18 && mx >= x && mx < x + W) {
            dragging = true; dragDX = mx - x; dragDY = my - y; return true;
        }
        int ty = y + 26;
        for (Category k : Category.values()) {
            if (mx >= x && mx < x + TAB_W && my >= ty && my < ty + 26) { cat = k; selected = null; scroll = 0; return true; }
            ty += 30;
        }
        int lx = x + TAB_W + 6, ly = y + 40 - scroll;
        for (Module m : list()) {
            if (mx >= lx && mx < lx + LIST_W && my >= ly && my < ly + 20 && my >= y + 40 && my < y + H - 6) {
                if (btn == 1) m.toggle(); else selected = m;
                return true;
            }
            ly += 24;
        }
        if (selected != null) {
            int px = lx + LIST_W + 10, pw = x + W - 8 - px;
            if (mx >= px && mx < px + pw && my >= y + 54 && my < y + 72) { selected.toggle(); return true; }
            if (mx >= px && mx < px + pw && my >= y + H - 34 && my < y + H - 18) { binding = true; return true; }
            int sy = y + 84;
            for (NumberSetting n : selected.settings) {
                if (mx >= px && mx < px + pw && my >= sy + 10 && my < sy + 22) {
                    draggedSlider = n; applySlider(mx, px, pw); return true;
                }
                sy += 28;
            }
        }
        return super.mouseClicked(mx, my, btn);
    }

    private void applySlider(double mx, int px, int pw) {
        double t = Math.max(0, Math.min(1, (mx - px) / pw));
        draggedSlider.set(draggedSlider.min + t * (draggedSlider.max - draggedSlider.min));
    }

    @Override public boolean mouseDragged(double mx, double my, int btn, double dx, double dy) {
        if (dragging) { x = (int) (mx - dragDX); y = (int) (my - dragDY); return true; }
        if (draggedSlider != null) {
            int px = x + TAB_W + 6 + LIST_W + 10, pw = x + W - 8 - px;
            applySlider(mx, px, pw); return true;
        }
        return super.mouseDragged(mx, my, btn, dx, dy);
    }

    @Override public boolean mouseReleased(double mx, double my, int btn) {
        dragging = false; draggedSlider = null;
        return super.mouseReleased(mx, my, btn);
    }

    @Override public boolean mouseScrolled(double mx, double my, double h, double v) {
        scroll -= (int) (v * 20);
        return true;
    }

    @Override public boolean keyPressed(int key, int scan, int mods) {
        if (binding && selected != null) {
            selected.key = key == GLFW.GLFW_KEY_ESCAPE || key == GLFW.GLFW_KEY_BACKSPACE ? -1 : key;
            binding = false; return true;
        }
        if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) { close(); return true; }
        return super.keyPressed(key, scan, mods);
    }
}
