# Nitro (Fabric, MC 1.21.4)
Build: run `gradle wrapper` once, then `./gradlew build`. The jar ends up in build/libs/.
Open GUI: Right Shift. LMB selects a module, RMB toggles it, scroll the list, "Bind" sets a key.
Add a module: subclass `Module`, register it in `ModuleManager`.
Categories: Combat (14), Render (2), Misc (9), Donut (empty).
Intended for singleplayer, your own server, and servers that allow client mods (e.g. anarchy servers).
Untested: written without compiling, so Yarn names (especially in the mixins) may need small fixes.
