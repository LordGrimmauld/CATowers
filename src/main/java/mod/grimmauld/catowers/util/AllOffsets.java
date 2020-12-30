package mod.grimmauld.catowers.util;

import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class AllOffsets {
	public static final List<Vec3i> offsets = new ArrayList<>();
	public static final List<Vec3i> straightOffsets = new ArrayList<>();

	static {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				offsets.add(new Vec3i(x, 0, z));
			}
		}
	}

	static {
		straightOffsets.add(new Vec3i(1, 0, 1));
		straightOffsets.add(new Vec3i(-1, 0, 1));
		straightOffsets.add(new Vec3i(1, 0, -1));
		straightOffsets.add(new Vec3i(-1, 0, -1));
	}
}
