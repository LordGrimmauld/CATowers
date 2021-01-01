package mod.grimmauld.catowers.decorator;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;

import java.util.Random;
import java.util.function.Consumer;

public class Decorator {

	public static void decorate(StructureMetaInf structure, IWorld world, BlockPos start, Random random, Consumer<ITextComponent> sendFeedback) {
		boolean filterFloatyBlocks = true;

		if (filterFloatyBlocks) {
			structure.filter(BlockMetaInf::isFloating);
		}

		structure.stream().map(start::add).forEach(pos -> world.setBlockState(pos, Blocks.STONE.getDefaultState(), 3));
	}
}
