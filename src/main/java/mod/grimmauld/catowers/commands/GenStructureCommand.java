package mod.grimmauld.catowers.commands;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import mod.grimmauld.catowers.generator.Generator;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

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
		System.out.println(source);
		System.out.println(pos);
		System.out.println(random);
		System.out.println();

		Generator.generate(source.getWorld(), pos, random);

		return 1;
	}
}
