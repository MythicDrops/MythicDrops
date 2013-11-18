package net.nunnerycode.bukkit.mythicdrops.api.common;

import org.bukkit.World;

public final class CuboidRegion {

	private final World world;
	private final double x1;
	private final double y1;
	private final double z1;
	private final double x2;
	private final double y2;
	private final double z2;

	public CuboidRegion(World world, double x1, double y1, double z1, double x2, double y2, double z2) {
		this.world = world;
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
	}

	public boolean withinRegion(World world, double x, double y, double z) {
		return !(world == null || this.world == null) && x >= getMinX() && x <= getMaxX() && y >= getMinY() && y <=
				getMaxY() && z >= getMinZ() && z <= getMaxZ();
	}

	public double getVolume() {
		return getRangeX() * getRangeY() * getRangeZ();
	}

	public double getRangeX() {
		return Math.abs(getMaxX() - getMinX());
	}

	public double getRangeY() {
		return Math.abs(getMaxY() - getMinY());
	}

	public double getRangeZ() {
		return Math.abs(getMaxZ() - getMinZ());
	}

	public double getMaxX() {
		return Math.max(x1, x2);
	}

	public double getMinX() {
		return Math.min(x1, x2);
	}

	public double getMaxY() {
		return Math.max(y1, y2);
	}

	public double getMinY() {
		return Math.min(y1, y2);
	}

	public double getMaxZ() {
		return Math.max(z1, z2);
	}

	public double getMinZ() {
		return Math.min(z1, z2);
	}

	public double getX1() {
		return x1;
	}

	public double getY1() {
		return y1;
	}

	public double getZ1() {
		return z1;
	}

	public double getX2() {
		return x2;
	}

	public double getY2() {
		return y2;
	}

	public double getZ2() {
		return z2;
	}

	public World getWorld() {
		return world;
	}
}
