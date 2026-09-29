package net.supernova.mightmayhem.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY = "key.category.might_mayhem.cultivation";
    public static final String KEY_OPEN_QI = "key.might_mayhem.open_qi";

    public static final KeyMapping OPEN_QI_KEY = new KeyMapping(
            KEY_OPEN_QI, KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B, KEY_CATEGORY);
}
