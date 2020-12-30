package mod.grimmauld.catowers.commands;

import com.mojang.brigadier.CommandDispatcher;
import mod.grimmauld.catowers.CATowers;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class AllCommands {
	public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
		commandDispatcher.register(Commands.literal(CATowers.MODID.toLowerCase()).then(GenStructureCommand.register()));
	}
}
