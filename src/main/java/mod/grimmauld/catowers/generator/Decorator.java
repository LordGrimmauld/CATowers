package mod.grimmauld.catowers.generator;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;

import java.util.Random;
import java.util.function.Consumer;

public class Decorator {

	public static void decorate(StructureMetaInf structure, IWorld world, BlockPos start, Random random, Consumer<ITextComponent> sendFeedback) {
		boolean filterFloatyBlocks = true;

		structure.update();

		if (filterFloatyBlocks) {
			structure.removeIf(BlockMetaInf::unsupported);
		}

		for (BlockMetaInf block : structure) {
			if (block.isSupportingBlock())
				world.setBlockState(block.getPos(), Blocks.STONE_BRICKS.getDefaultState(), 3);
			else
				world.setBlockState(block.getPos(), Blocks.STONE.getDefaultState(), 3);
		}
	}
}
