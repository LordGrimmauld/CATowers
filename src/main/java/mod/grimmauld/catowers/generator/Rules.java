package mod.grimmauld.catowers.generator;

import static mod.grimmauld.catowers.generator.Rule.isAlive;

@SuppressWarnings("unused")
public class Rules {
	private static final Rule CONWAYS = new Rule((c, a) -> c == 3 || a && c == 2, true).register(5);
	private static final Rule HIGHLIFE = new Rule((c, a) -> c == 3 || !a && c == 6 || a && c == 2, true).register(4);
	private static final Rule PYRAMID = new Rule((c, a) -> c == 3 || c == 4 || a && c == 2, false).register(4);

	private static final Rule B36S013468 = new Rule((c, a) -> c == 3 || c == 6 || a && c == 0 || a && c == 1 || a && c == 4 || a && c == 8, false).register(3);
	private static final Rule B367S02347 = new Rule((c, a) -> c == 3 || !a && c == 6 || c == 7 || a && c == 0 || a && c == 2 || a && c == 4, false).register(2);
	private static final Rule B34S2356 = new Rule((c, a) -> c == 3 || !a && c == 4 || a && c == 2 || a && c == 5 || a && c == 6, true).register(3);
	private static final Rule B368S245 = new Rule((c, a) -> !a && c == 3 || !a && c == 6 || !a && c == 8 || a && c == 2 || a && c == 4 || a && c == 5, true).register(8);
	private static final Rule B36S125 = new Rule((c, a) -> !a && c == 3 || !a && c == 6 || a && c == 1 || a && c == 2 || a && c == 5, false).register(4);
	private static final Rule B3568S148 = new Rule((c, a) -> !a && c == 3 || !a && c == 5 || !a && c == 6 || !a && c == 8 || a && c == 1 || a && c == 4 || a && c == 8, false).register(4);
	private static final Rule B3S1245 = new Rule((c, a) -> !a && c == 3 || a && c == 1 || a && c == 2 || a && c == 4 || a && c == 5, false).register(8);
	private static final Rule B3567S13468 = new Rule((c, a) -> c == 3 || !a && c == 5 || c == 6 || !a && c == 7 || a && c == 1 || a && c == 4 || a && c == 8, false).register(5);
	private static final Rule B356S16 = new Rule((c, a) -> !a && c == 3 || !a && c == 5 || c == 6 || a && c == 1, false).register(5);
	private static final Rule B3468S123 = new Rule((c, a) -> c == 3 || !a && c == 4 || !a && c == 6 || !a && c == 8 || a && c == 1 || a && c == 2, false).register(3);
	private static final Rule B35678S015678 = new Rule((c, a) -> !a && c == 3 || c == 5 || c == 6 || c == 7 || c == 8 || a && c == 0 || a && c == 1, false).register(2);
	private static final Rule B35678S0156 = new Rule((c, a) -> !a && c == 3 || c == 5 || c == 6 || !a && c == 7 || !a && c == 8 || a && c == 0 || a && c == 1, false).register(10);
	private static final Rule B26S12368 = new Rule((c, a) -> c == 2 || c == 6 || a && c == 1 || a && c == 3 || a && c == 8, false).register(5);
	private static final Rule B248S45 = new Rule((c, a) -> !a && c == 2 || c == 4 || !a && c == 8 || a && c == 5, false).register(5);
	private static final Rule B2457S013458 = new Rule((c, a) -> !a && c == 2 || c == 4 || c == 5 || !a && c == 7 || a && c == 0 || a && c == 1 || a && c == 3 || a && c == 8, false).register(1);
	private static final Rule B45S2345 = new Rule((c, a) -> c == 4 || c == 5 || a && c == 2 || a && c == 3, false).register(6);

	private static final Rule GRIMM2 = new Rule((x, layers, offset) -> {
		boolean a = isAlive(layers, offset, 1);
		boolean c = isAlive(layers, offset, 3);
		boolean d = isAlive(layers, offset, 4);
		return x == 2 && a || x == 3 || x == 4 && c || x == 5 && d;
	}, false).register(4);

	private static final Rule GRIMM3 = new Rule((x, layers, offset) -> {
		boolean a = isAlive(layers, offset, 1);
		boolean b = isAlive(layers, offset, 2);
		boolean c = isAlive(layers, offset, 3);
		boolean d = isAlive(layers, offset, 4);
		return x == 2 && a || x == 3 || x == 4 && b || x == 5 && c || x == 6 && d;
	}, false).register(4);

	public static void register(){}
}
