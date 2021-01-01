package mod.grimmauld.catowers.decorator;

import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;

import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Decorator {

	public static void decorate(Set<Vec3i> structure, IWorld world, BlockPos start, Random random, Consumer<ITextComponent> sendFeedback) {
		boolean filterFloatyBlocks = true;

		if (filterFloatyBlocks) {
			Set<Vec3i> finalStructure = structure;
			structure = structure.stream().filter(pos -> Arrays.stream(Direction.values()).map(d -> pos.offset(d, 1)).anyMatch(finalStructure::contains)).collect(Collectors.toSet());
		}

		structure.stream().map(start::add).forEach(pos -> world.setBlockState(pos, Blocks.STONE.getDefaultState(), 3));
	}
}
