package mod.grimmauld.catowers.commands;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import mod.grimmauld.catowers.decorator.Decorator;
import mod.grimmauld.catowers.decorator.StructureMetaInf;
import mod.grimmauld.catowers.generator.Generator;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.util.Random;
import java.util.function.Consumer;

public class GenStructureCommand {
	public static ArgumentBuilder<CommandSource, ?> register() {
		return Commands.literal("generate")
			.requires(commandSource -> commandSource.hasPermissionLevel(2))
			.then(Commands.argument("pos", BlockPosArgument.blockPos())
				.executes(ctx -> run(ctx.getSource(), BlockPosArgument.getBlockPos(ctx, "pos"), ctx.getSource().getWorld().getRandom()))
				.then(Commands.argument("seed", LongArgumentType.longArg())
					.executes(ctx -> run(ctx.getSource(), BlockPosArgument.getBlockPos(ctx, "pos"), new Random(LongArgumentType.getLong(ctx, "seed"))))));
	}

	private static int run(CommandSource source, BlockPos pos, Random random) {
		Consumer<ITextComponent> feedback = component -> source.sendFeedback(component, true);
		StructureMetaInf structure = Generator.generate(source, pos, random);
		Decorator.decorate(structure, source.getWorld(), pos, random, feedback);
		return 1;
	}
}
