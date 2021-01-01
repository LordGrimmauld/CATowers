package mod.grimmauld.catowers.decorator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;

import java.util.HashSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StructureMetaInf extends HashSet<BlockMetaInf> {
	private final IWorld world;
	private final BlockPos anchor;

	public StructureMetaInf(IWorld world, BlockPos anchor) {
		this.world = world;
		this.anchor = anchor;
	}

	public static StructureMetaInf toStructureMetaInf(Stream<Vec3i> stream, IWorld world, BlockPos anchor) {
		StructureMetaInf inf = new StructureMetaInf(world, anchor);
		stream.forEachOrdered(inf::add);
		return inf;
	}

	public void add(Vec3i offset) {
		this.add(new BlockMetaInf(this, offset));
	}


	public BlockPos getAnchor() {
		return anchor;
	}

	public IWorld getWorld() {
		return world;
	}

	public StructureMetaInf filter(Predicate<BlockMetaInf> test) {
		this.removeIf(offset -> !test.test(offset));
		return this;
	}
}
