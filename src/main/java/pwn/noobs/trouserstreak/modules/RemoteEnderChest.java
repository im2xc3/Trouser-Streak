package pwn.noobs.trouserstreak.modules;

import meteordevelopment.meteorclient.events.meteor.KeyInputEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import pwn.noobs.trouserstreak.Trouser;

import java.util.List;

import com.mojang.blaze3d.platform.InputConstants;

public class RemoteEnderChest extends Module {
    private final SettingGroup sgGeneral = settings.createGroup("RemoteEnderChest");
    private final SettingGroup sgItemSaver = settings.createGroup("ItemSaver");
    
    private final Setting<Keybind> toggleGui = sgGeneral.add(new KeybindSetting(
            "GUI Key (Press it)",
            "Key to toggle Ender Chest GUI.",
            Keybind.fromKey(InputConstants.KEY_LALT),
            value -> {
            },
            value -> {
            },
            null,
            () -> {
            }
    ));
    private final Setting<Boolean> invKeyBlocker = sgGeneral.add(new BoolSetting.Builder()
            .name("inventory-key-modifier")
            .description("Make the Inventory Key open the EnderChest GUI when you have one saved. This helps to prevent you accidentally breaking the link.")
            .defaultValue(true)
            .build()
    );
    private final Setting<Boolean> enableItemSaver = sgItemSaver.add(new BoolSetting.Builder()
            .name("enable-item-saver")
            .description("Automatically moves items in your inventory into the linked Ender Chest when low health.")
            .defaultValue(true)
            .build()
    );
    private final Setting<Double> healthThreshold = sgItemSaver.add(new DoubleSetting.Builder()
            .name("health-threshold")
            .description("Move the items to the open Ender Chest when health is equal to or less than this value (2.0 = 1 heart).")
            .defaultValue(4.0)
            .min(0.0)
            .max(20.0)
            .sliderRange(0.0, 20.0)
            .visible(enableItemSaver::get)
            .build()
    );
    private final Setting<Boolean> totemCheck = sgItemSaver.add(new BoolSetting.Builder()
            .name("check-for-totem")
            .description("If you are low health and are holding a Totem of Undying, do not store the items.")
            .defaultValue(true)
            .visible(enableItemSaver::get)
            .build()
    );
    private final Setting<List<Item>> items = sgItemSaver.add(new ItemListSetting.Builder()
            .name("Items to save")
            .description("These items will be stored.")
            .visible(enableItemSaver::get)
            .build()
    );
    private final Setting<Boolean> keepInv = sgItemSaver.add(new BoolSetting.Builder()
            .name("keep-inventory")
            .description("Try to save the whole inventory. The Items to save list will be prioritized.")
            .defaultValue(true)
            .visible(enableItemSaver::get)
            .build()
    );
    private final Setting<Keybind> itemSaverHotkey = sgItemSaver.add(new KeybindSetting(
            "Item Saver HotKey",
            "You can quickly store items when pressing this button.",
            Keybind.fromKey(InputConstants.KEY_RALT),
            value -> {
            },
            value -> {
            },
            null,
            () -> {
            }
    ));
    
    public RemoteEnderChest() {
        super(Trouser.Main, "RemoteEnderChest", "Access your enderchest anywhere and move freely while it is open.");
    }
}