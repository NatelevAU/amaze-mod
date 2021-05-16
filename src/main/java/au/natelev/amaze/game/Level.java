package au.natelev.amaze.game;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final ServerWorld serverWorld;
    private final int width;
    private final int height;
    private int[][] tileMap = null;

    private final BlockPos gameOrigin;
    private final BlockPos levelOrigin;

    private final List<BlockPos> unpaintedTiles = new ArrayList<>();

    public Level(ServerWorld serverWorld, int width, int height, BlockPos gameOrigin) {
        this.serverWorld = serverWorld;
        this.width = width;
        this.height = height;
        this.gameOrigin = gameOrigin;
        this.levelOrigin = getLevelOrigin(width, 0, height);
    }



    protected int getWidth() { return this.width; }
    protected int getHeight() { return this.height; }
    protected BlockPos getOrigin() { return this.levelOrigin; }



    protected void paint(BlockPos blockPos) {
        unpaintedTiles.remove(blockPos);
    }

    protected boolean isFinished() {
        return unpaintedTiles.isEmpty();
    }

    protected void setTiles(int[][] tileMap) {
        this.tileMap = tileMap;
        resetTiles();
    }

    protected void resetTiles() {
        unpaintedTiles.clear();
        BlockPos tilePos = null;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (tileMap[i][j] > 0)  {
                    tilePos = levelOrigin.offset(i, 0, j);
                    unpaintedTiles.add(tilePos);
                }
            }
        }
    }

    protected void buildMap(int prevWidth, int prevHeight) {
        if (prevWidth > width || prevHeight > height) {
            int heightDiff = prevHeight - height;
            BlockPos currPos = getLevelOrigin(prevWidth, -1, prevHeight);
            for (int i = 0; i < prevWidth; i++) {
                for (int j = 0; j < prevHeight; j++) {
                    if (j == heightDiff) {
                        j += height - heightDiff;
                        currPos = currPos.offset(0, 0, height - heightDiff);
                        continue;
                    }
                    buildTile(currPos, 1);
                    currPos = currPos.offset(0, 0, 1);
                }
                currPos = currPos.offset(1, 0, levelOrigin.getZ() - currPos.getZ());
            }
        }
        resetMap();
    }

    protected void resetMap() {
        BlockPos currPos = levelOrigin;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                buildTile(currPos, tileMap[i][j]);
                buildTile(currPos.below(), -1);
                currPos = currPos.offset(0, 0, 1);
            }
            currPos = currPos.offset(1, 0, levelOrigin.getZ() - currPos.getZ());
        }
        resetTiles();
    }



    private void buildTile(BlockPos pos, int tileState) {
        final BlockState block;
        switch (tileState) {
            case -1:
                block = Blocks.BLACK_CONCRETE.defaultBlockState();
                break;
            case 0:
                block = Blocks.WHITE_CONCRETE.defaultBlockState();
                break;
            case 1:
            case 2:
                block = Blocks.AIR.defaultBlockState();
                break;
            default:
                return;
        }
        serverWorld.setBlockAndUpdate(pos, block);
    }

    private BlockPos getLevelOrigin(int width, int y, int height) {
        return new BlockPos(gameOrigin.getX() - width/2, gameOrigin.getY() + y, gameOrigin.getZ() - height/2);
    }
}
