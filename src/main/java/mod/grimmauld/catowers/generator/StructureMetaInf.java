package mod.grimmauld.catowers.generator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;

import java.util.HashSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StructureMetaInf extends HashSet<BlockMetaInf> {
	private final IWorld world;
	private final BlockPos anchor;

	public int getMaxOffsetNorth() {
		return maxOffsetNorth;
	}

	public int getMaxOffsetSouth() {
		return maxOffsetSouth;
	}

	public int getMaxOffsetTop() {
		return maxOffsetTop;
	}

	public int getMaxOffsetBottom() {
		return maxOffsetBottom;
	}

	public int getMaxOffsetWest() {
		return maxOffsetWest;
	}

	public int getMaxOffsetEast() {
		return maxOffsetEast;
	}

	public int maxOffsetNorth = 0;
	public int maxOffsetSouth = 0;
	public int maxOffsetTop = 0;
	public int maxOffsetBottom = 0;
	public int maxOffsetWest = 0;
	public int maxOffsetEast = 0;

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

	public void update() {
		for (BlockMetaInf blockMetaInf: this) {
			blockMetaInf.updateStructure(this);
			maxOffsetEast = Math.max(maxOffsetEast, blockMetaInf.getX());
			maxOffsetTop = Math.max(maxOffsetTop, blockMetaInf.getY());
			maxOffsetSouth = Math.max(maxOffsetSouth, blockMetaInf.getZ());
			maxOffsetWest = Math.min(maxOffsetWest, blockMetaInf.getX());
			maxOffsetBottom = Math.min(maxOffsetBottom, blockMetaInf.getY());
			maxOffsetNorth = Math.min(maxOffsetNorth, blockMetaInf.getZ());
		}
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
