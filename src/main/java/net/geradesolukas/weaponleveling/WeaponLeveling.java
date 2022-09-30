package net.geradesolukas.weaponleveling;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.geradesolukas.weaponleveling.config.WeaponlevelingConfig;
import net.geradesolukas.weaponleveling.event.cancelling.BreakBlockCancel;
import net.geradesolukas.weaponleveling.event.cancelling.InteractBlockCancel;
import net.geradesolukas.weaponleveling.event.cancelling.InteractItemCancel;
import net.geradesolukas.weaponleveling.networking.Networking;
import net.geradesolukas.weaponleveling.server.command.ItemLevelCommand;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeaponLeveling implements ModInitializer {

	public static final String MODID = "weaponleveling";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);


	@Override
	public void onInitialize() {

		ModLoadingContext.registerConfig(MODID, ModConfig.Type.SERVER, WeaponlevelingConfig.Server.SPEC, "weaponleveling-server.toml");
		ModLoadingContext.registerConfig(MODID, ModConfig.Type.CLIENT, WeaponlevelingConfig.Client.SPEC, "weaponleveling-client.toml");

		CommandRegistrationCallback.EVENT.register(ItemLevelCommand::register);

		Networking.registerC2SPackets();
		AttackBlockCallback.EVENT.register(new BreakBlockCancel());
		UseBlockCallback.EVENT.register(new InteractBlockCancel());
		UseItemCallback.EVENT.register(new InteractItemCancel());
	}
}
