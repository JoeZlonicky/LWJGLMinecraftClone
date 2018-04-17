package terrain;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import entities.Block;
import entities.DirtBlock;

public class Chunk {

	public int chunk_x;
	public int chunk_z;
	
	private static final int SIZE = 16;
	private Block[][][] blockArray = new Block[SIZE][SIZE][SIZE];
	private List<Block> blocksToRender = new ArrayList<Block>();

	public Chunk(int x, int z) {
		this.chunk_x = x;
		this.chunk_z = z;
		generateBlocks();
	}

	private void generateBlocks() {
		for (int x = 0; x < SIZE; ++x) {
			for (int y = 0; y < SIZE; ++y) {
				for (int z = 0; z < SIZE; ++z) {
					Block dirtBlock = new DirtBlock(new Vector3f(chunk_x + x * 2, y * 2, chunk_z + z * 2));
					if (!(x == 4 && y == 7 && z == 6)) {
						addBlock(dirtBlock, x, y, z);
					}
				}
			}
		}
	}

	public void addBlock(Block block, int x, int y, int z) {
		blockArray[x][y][z] = block;
		updateBlockAndNeighbors(x, y, z);
	}
	
	public void removeBlock(int x, int y, int z) {
		blocksToRender.remove(blockArray[x][y][z]);
		blockArray[x][y][z] = null;
		updateBlockAndNeighbors(x, y, z);
	}

	public void updateBlockAndNeighbors(int x, int y, int z) {
		checkNeighborFlags(x, y, z);
		checkNeighborFlags(x + 1, y, z);
		checkNeighborFlags(x - 1, y, z);
		checkNeighborFlags(x, y + 1, z);
		checkNeighborFlags(x, y - 1, z);
		checkNeighborFlags(x, y, z + 1);
		checkNeighborFlags(x, y, z - 1);
	}
	
	public void checkNeighborFlags(int x, int y, int z) {
		if (!spotInBlockArrayFilled(x, y, z)) return;
		
		Block block = blockArray[x][y][z];
		if (spotInBlockArrayFilled(x + 1, y, z))
			block.hasXPNeighbor = true;
		if (spotInBlockArrayFilled(x - 1, y, z))
			block.hasXMNeighbor = true;
		if (spotInBlockArrayFilled(x, y + 1, z))
			block.hasYPNeighbor = true;
		if (spotInBlockArrayFilled(x, y - 1, z))
			block.hasYMNeighbor = true;
		if (spotInBlockArrayFilled(x, y, z + 1))
			block.hasZPNeighbor = true;
		if (spotInBlockArrayFilled(x, y, z - 1))
			block.hasZMNeighbor = true;
		
		if (block.shouldRender() && !blocksToRender.contains(block)) {
			blocksToRender.add(block);
		} else if (!block.shouldRender() && blocksToRender.contains(block)) {
			blocksToRender.remove(block);
		}
	}
	
	private boolean spotInBlockArrayFilled(int x, int y, int z) {
		if (x >= 0 && x < SIZE && y >= 0 && y < SIZE && z >= 0 && z < SIZE) {
			if (blockArray[x][y][z] == null) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public List<Block> getBlocksToRender() {
		return blocksToRender;
	}
}
