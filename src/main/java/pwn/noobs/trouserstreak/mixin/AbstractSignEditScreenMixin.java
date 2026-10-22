


package pwn.noobs.trouserstreak.mixin;

import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.KeybindContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignTextSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pwn.noobs.trouserstreak.modules.NoModDetection;

import java.util.List;

@Mixin(AbstractSignEditScreen.class)
public abstract class AbstractSignEditScreenMixin {
    @Shadow @Final private String[] messages;
    
    @Inject(
            method = "<init>(Lnet/minecraft/world/level/block/entity/SignBlockEntity;Lnet/minecraft/world/level/block/entity/SignTextSlot;ZLnet/minecraft/network/chat/Component;)V",
            at = @At("TAIL")
    )
    private void trouserstreak$filterSignText(
            SignBlockEntity sign,
            SignTextSlot slot,
            boolean shouldFilter,
            Component title,
            CallbackInfo ci
    ) {
        NoModDetection module = Modules.get().get(NoModDetection.class);
        if (module == null || !module.isActive() || !module.filterSign.get()) return;
        
        List<Component> lines = sign.getText(slot).getMessages(shouldFilter);
        for (int i = 0; i < Math.min(messages.length, lines.size()); i++) {
            Component component = lines.get(i);
            if (component.getContents() instanceof TranslatableContents translatable) {
                String fallback = translatable.getFallback();
                messages[i] = fallback != null ? fallback : translatable.getKey();
            } else if (component.getContents() instanceof KeybindContents) {
                messages[i] = "";
            } else {
                messages[i] = component.getString();
            }
        }
    }
}

