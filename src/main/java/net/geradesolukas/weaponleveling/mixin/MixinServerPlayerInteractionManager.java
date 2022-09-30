package net.geradesolukas.weaponleveling.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.geradesolukas.weaponleveling.util.ItemUtils;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ServerPlayerInteractionManager.class)
public class MixinServerPlayerInteractionManager {

    @Shadow @Final protected ServerPlayerEntity player;


    //@Inject(
    //        method = "tryBreakBlock",
    //        at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;canMine(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)Z"),
    //        locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    //private void injectBroken(BlockPos pos, CallbackInfoReturnable<Boolean> cir, BlockState blockState) {
    //    if(ItemUtils.isBroken(this.player.getMainHandStack())) {return false;}
    //}

    @ModifyExpressionValue(
            method = "Lnet/minecraft/server/network/ServerPlayerInteractionManager;tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;canMine(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)Z"))
    private boolean injectBroken(boolean original, BlockPos pos) {
            return ItemUtils.isBroken(this.player.getMainHandStack());
    }
}
