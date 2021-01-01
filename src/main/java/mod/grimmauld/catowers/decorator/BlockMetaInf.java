package mod.grimmauld.catowers.decorator;

import net.minecraft.util.Direction;
import net.minecraft.util.LazyValue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Arrays;

public class BlockMetaInf extends Vec3i {
	private final StructureMetaInf structure;
	private final Vec3i offset;
	private final BlockPos pos;
	private final LazyValue<Boolean> floating;

	public BlockMetaInf(StructureMetaInf structure, Vec3i offset) {
		super(offset.getX(), offset.getY(), offset.getZ());
		this.structure = structure;
		this.offset = offset;
		pos = structure.getAnchor().add(offset);
		floating = new LazyValue<>(() -> Arrays.stream(Direction.values()).map(d -> offset.offset(d, 1)).anyMatch(structure::contains));
	}

	public boolean isFloating() {
		return floating.getValue();
	}
}
