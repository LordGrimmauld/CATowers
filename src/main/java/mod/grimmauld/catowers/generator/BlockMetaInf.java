package mod.grimmauld.catowers.generator;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyValue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import javax.annotation.Nullable;
import java.util.Arrays;

public class BlockMetaInf extends Vec3i {
	private StructureMetaInf structure;
	private final BlockPos pos;
	private final LazyValue<Boolean> unsupported;
	private final LazyValue<Vec3i> northNeighbor;
	private final LazyValue<Vec3i> southNeighbor;
	private final LazyValue<Vec3i> topNeighbor;
	private final LazyValue<Vec3i> bottomNeighbor;
	private final LazyValue<Vec3i> westNeighbor;
	private final LazyValue<Vec3i> eastNeighbor;

	public BlockMetaInf(StructureMetaInf structure, Vec3i offset) {
		this(structure, offset.getX(), offset.getY(), offset.getZ());
	}

	public BlockMetaInf(StructureMetaInf structure, int x, int y, int z) {
		super(x, y, z);
		this.structure = structure;
		pos = structure.getAnchor().add(this);

		// Lazy values (for performance)
		unsupported = new LazyValue<>(() -> Arrays.stream(Direction.values()).map(d -> offset(d, 1)).noneMatch(structure::contains));
		eastNeighbor = new LazyValue<>(this::calculateEastNeighbor);
		topNeighbor = new LazyValue<>(this::calculateTopNeighbor);
		southNeighbor = new LazyValue<>(this::calculateSouthNeighbor);
		westNeighbor = new LazyValue<>(this::calculateWestNeighbor);
		bottomNeighbor = new LazyValue<>(this::calculateBottomNeighbor);
		northNeighbor = new LazyValue<>(this::calculateNorthNeighbor);
	}

	public BlockPos getPos() {
		return pos;
	}

	@Nullable
	private BlockMetaInf calculateNorthNeighbor() {
		for (int z = structure.getMaxOffsetNorth(); z < getZ(); z++) {
			BlockMetaInf test = structure.getFromOffset(new Vec3i(getX(), getY(), z));
			if(test != null)
				return test;
		}
		return null;
	}

	@Nullable
	private BlockMetaInf calculateSouthNeighbor() {
		for (int z = getZ() + 1; z <= structure.getMaxOffsetSouth(); z++) {
			BlockMetaInf test = structure.getFromOffset(new Vec3i(getX(), getY(), z));
			if(test != null)
				return test;
		}
		return null;
	}

	@Nullable
	private BlockMetaInf calculateTopNeighbor() {
		for (int y = getY() + 1; y <= structure.getMaxOffsetTop(); y++) {
			BlockMetaInf test = structure.getFromOffset(new Vec3i(getX(), y, getZ()));
			if(test != null)
				return test;
		}
		return null;
	}

	@Nullable
	private BlockMetaInf calculateBottomNeighbor() {
		for (int y = structure.getMaxOffsetBottom(); y < getY(); y++) {
			BlockMetaInf test = structure.getFromOffset(new Vec3i(getX(), y, getZ()));
			if(test != null)
				return test;
		}
		return null;
	}

	@Nullable
	private BlockMetaInf calculateWestNeighbor() {
		for (int x = structure.getMaxOffsetWest(); x < getY(); x++) {
			BlockMetaInf test = structure.getFromOffset(new Vec3i(x, getY(), getZ()));
			if(test != null)
				return test;
		}
		return null;
	}

	@Nullable
	private BlockMetaInf calculateEastNeighbor() {
		for (int x = getX() + 1; x <= structure.getMaxOffsetEast(); x++) {
			BlockMetaInf test = structure.getFromOffset(new Vec3i(x, getY(), getZ()));
			if(test != null)
				return test;
		}
		return null;
	}

	public boolean unsupported() {
		return unsupported.getValue();
	}

	public boolean isSupportingBlock() {
		return Block.hasSolidSideOnTop(structure.getWorld(), pos.down()) && bottomNeighbor.getValue() == null;
	}

	public void updateStructure(StructureMetaInf structure) {
		this.structure = structure;
	}
}
