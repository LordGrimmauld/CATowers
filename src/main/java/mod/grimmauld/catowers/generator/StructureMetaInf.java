package mod.grimmauld.catowers.generator;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StructureMetaInf implements Set<BlockMetaInf> {
	private final IWorld world;
	private final BlockPos anchor;
	private final HashMap<Vec3i, BlockMetaInf> map;

	public int maxOffsetNorth = 0;
	public int maxOffsetSouth = 0;
	public int maxOffsetTop = 0;
	public int maxOffsetBottom = 0;
	public int maxOffsetWest = 0;
	public int maxOffsetEast = 0;

	private final HashMap<Integer, Set<BlockMetaInf>> layersX = new HashMap<>();
	private final HashMap<Integer, Set<BlockMetaInf>> layersY = new HashMap<>();
	private final HashMap<Integer, Set<BlockMetaInf>> layersZ = new HashMap<>();

	public StructureMetaInf(IWorld world, BlockPos anchor) {
		this.world = world;
		this.anchor = anchor;
		this.map = new HashMap<>();
	}

	public static StructureMetaInf toStructureMetaInf(Stream<Vec3i> stream, IWorld world, BlockPos anchor) {
		StructureMetaInf inf = new StructureMetaInf(world, anchor);
		stream.forEach(inf::add);
		return inf;
	}

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

	public void add(Vec3i offset) {
		map.put(offset, new BlockMetaInf(this, offset));
	}

	public void update() {
		maxOffsetNorth = 0;
		maxOffsetSouth = 0;
		maxOffsetTop = 0;
		maxOffsetBottom = 0;
		maxOffsetWest = 0;
		maxOffsetEast = 0;

		layersX.clear();
		layersY.clear();
		layersZ.clear();

		for (BlockMetaInf blockMetaInf : map.values()) {
			blockMetaInf.updateStructure(this);
			int x = blockMetaInf.getX();
			int y = blockMetaInf.getY();
			int z = blockMetaInf.getZ();

			Set<BlockMetaInf> layer = layersX.getOrDefault(x, new HashSet<>());
			layer.add(blockMetaInf);
			layersX.put(x, layer);

			layer = layersY.getOrDefault(y, new HashSet<>());
			layer.add(blockMetaInf);
			layersY.put(y, layer);

			layer = layersZ.getOrDefault(z, new HashSet<>());
			layer.add(blockMetaInf);
			layersZ.put(z, layer);

			maxOffsetEast = Math.max(maxOffsetEast, x);
			maxOffsetTop = Math.max(maxOffsetTop, y);
			maxOffsetSouth = Math.max(maxOffsetSouth, z);
			maxOffsetWest = Math.min(maxOffsetWest, x);
			maxOffsetBottom = Math.min(maxOffsetBottom, y);
			maxOffsetNorth = Math.min(maxOffsetNorth, z);
		}
	}

	@Nullable
	public BlockMetaInf getFromOffset(Vec3i offset) {
		return map.get(offset);
	}

	public BlockPos getAnchor() {
		return anchor;
	}

	public IWorld getWorld() {
		return world;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return map.containsKey(o);
	}

	public boolean contains(Vec3i o) {
		return map.containsKey(o);
	}

	@Override
	public Iterator<BlockMetaInf> iterator() {
		return map.values().iterator();
	}

	@Override
	public Object[] toArray() {
		return map.values().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return map.values().toArray(a);
	}

	@Override
	public boolean add(BlockMetaInf blockMetaInf) {
		map.put(blockMetaInf, blockMetaInf);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		return map.containsKey(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return map.keySet().containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends BlockMetaInf> c) {
		c.forEach(this::add);
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		this.removeIf(c::contains);
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		c.forEach(this::remove);
		return true;
	}

	@Override
	public void clear() {
		map.clear();
	}
}
