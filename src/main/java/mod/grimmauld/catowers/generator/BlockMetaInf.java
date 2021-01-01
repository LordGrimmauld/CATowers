package mod.grimmauld.catowers.generator;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyValue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Arrays;

public class BlockMetaInf extends Vec3i {
	private StructureMetaInf structure;
	private final BlockPos pos;
	private final LazyValue<Boolean> supported;
	private final LazyValue<Boolean> openNorth;
	private final LazyValue<Boolean> openSouth;
	private final LazyValue<Boolean> openTop;
	private final LazyValue<Boolean> openBottom;
	private final LazyValue<Boolean> openWest;
	private final LazyValue<Boolean> openEast;

	public BlockMetaInf(StructureMetaInf structure, Vec3i offset) {
		super(offset.getX(), offset.getY(), offset.getZ());
		this.structure = structure;
		pos = structure.getAnchor().add(offset);

		// Lazy values (for performance)
		supported = new LazyValue<>(() -> Arrays.stream(Direction.values()).map(d -> offset.offset(d, 1)).anyMatch(structure::contains));
		openEast = new LazyValue<>(this::isOpenEast);
		openTop = new LazyValue<>(this::isOpenTop);
		openSouth = new LazyValue<>(this::isOpenSouth);
		openWest = new LazyValue<>(this::isOpenWest);
		openBottom = new LazyValue<>(this::isOpenBottom);
		openNorth = new LazyValue<>(this::isOpenBottom);
	}

	public BlockPos getPos() {
		return pos;
	}

	private Boolean isOpenNorth() {
		for (int z = structure.getMaxOffsetNorth(); z < getZ(); z++) {
			if(structure.contains(new Vec3i(getX(), getY(), z)))
				return false;
		}
		return true;
	}

	private Boolean isOpenSouth() {
		for (int z = getZ() + 1; z <= structure.getMaxOffsetSouth(); z++) {
			if(structure.contains(new Vec3i(getX(), getY(), z)))
				return false;
		}
		return true;
	}

	private Boolean isOpenTop() {
		for (int y = getY() + 1; y <= structure.getMaxOffsetTop(); y++) {
			if(structure.contains(new Vec3i(getX(), y, getZ())))
				return false;
		}
		return true;
	}

	private Boolean isOpenBottom() {
		for (int y = structure.getMaxOffsetBottom(); y < getY(); y++) {
			if(structure.contains(new Vec3i(getX(), y, getZ())))
				return false;
		}
		return true;
	}

	private Boolean isOpenWest() {
		for (int x = structure.getMaxOffsetWest(); x < getY(); x++) {
			if(structure.contains(new Vec3i(x, getY(), getZ())))
				return false;
		}
		return true;
	}

	private Boolean isOpenEast() {
		for (int x = getX() + 1; x <= structure.getMaxOffsetEast(); x++) {
			if(structure.contains(new Vec3i(x, getY(), getZ())))
				return false;
		}
		return true;
	}

	public boolean isSupported() {
		return supported.getValue();
	}

	public boolean isSupportingBlock() {
		return Block.hasSolidSideOnTop(structure.getWorld(), pos.down()) && openBottom.getValue();
	}

	public void updateStructure(StructureMetaInf structure) {
		this.structure = structure;
	}
}
