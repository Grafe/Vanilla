/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.world.generator.normal.populator;

import java.util.Random;

import org.spout.api.Spout;
import org.spout.api.generator.Populator;
import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.world.generator.structure.stronghold.Stronghold;

public class StrongholdPopulator extends Populator {
	private static final int DISTANCE = 896;
	private static final int VARIATION = 256;
	private static final int BASE_Y = 35;
	private static final int RAND_Y = 11;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final int blockX = chunk.getBlockX();
		final int blockZ = chunk.getBlockZ();
		final int absBlockX = Math.abs(blockX);
		final int absBlockZ = Math.abs(blockZ);
		if (absBlockX <= DISTANCE || absBlockX > DISTANCE + Chunk.BLOCKS.SIZE
				|| absBlockZ <= DISTANCE || absBlockZ > DISTANCE + Chunk.BLOCKS.SIZE) {
			return;
		}
		final World world = chunk.getWorld();
		final int excludedQuadrant = new Random(world.getSeed()).nextInt(4);
		switch (excludedQuadrant) {
			case 0:
				if (blockX > 0 && blockZ > 0) {
					return;
				}
				break;
			case 1:
				if (blockX > 0 && blockZ < 0) {
					return;
				}
				break;
			case 2:
				if (blockX < 0 && blockZ < 0) {
					return;
				}
				break;
			case 3:
				if (blockX < 0 && blockZ > 0) {
					return;
				}
				break;
		}
		// This means only three strongholds per world, 1 per quadrant, except one excluded quadrant.
		// All are near + or - 896 on x and z (by + or - 256 on x and z).
		random = WorldGeneratorUtils.getRandom(world, blockX / absBlockX, 0,
				blockZ / absBlockZ, 57845);
		final Stronghold stronghold = new Stronghold(random);
		final int x = blockX + random.nextInt(2 * VARIATION + 1) - VARIATION;
		final int y = random.nextInt(RAND_Y) + BASE_Y;
		final int z = blockZ + random.nextInt(2 * VARIATION + 1) - VARIATION;
		if (stronghold.canPlaceObject(world, x, y, z)) {
			stronghold.placeObject(world, x, y, z);
			if (Spout.debugMode()) {
				Spout.log("Placed stronghold at: (" + x + ", " + y + ", " + z + ")");
			}
		}
	}
}
