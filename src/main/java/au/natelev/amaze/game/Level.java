package au.natelev.amaze.game;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final ServerWorld serverWorld;
    private final int height;
    private final int width;
    private int[][] tileMap = null;

    private final BlockPos gameOrigin;
    private final BlockPos levelOrigin;

    private final List<BlockPos> unpaintedTiles = new ArrayList<>();

    public Level(ServerWorld serverWorld, int height, int width, BlockPos gameOrigin) {
        this.serverWorld = serverWorld;
        this.height = height;
        this.width = width;
        this.gameOrigin = gameOrigin;
        this.levelOrigin = getLevelOrigin(height, 0, width);
    }



    protected int getHeight() { return this.height; }
    protected int getWidth() { return this.width; }



    protected void paint(BlockPos blockPos) {
        unpaintedTiles.remove(blockPos);
    }

    protected boolean isFinished() {
        return unpaintedTiles.isEmpty();
    }

    protected BlockPos getBallOrigin() {
        for (int i = 0; i < height; i++) {
            for (int j = width; j > 0;) {
                j--;
                if (tileMap[height -i-1][width -j-1] > 0) {
                    return levelOrigin.offset(j, 0, i);
                }
            }
        }
        return null;
    }

    protected void setTiles(int[][] tileMap) {
        this.tileMap = tileMap;
        resetTiles();
    }

    protected void resetTiles() {
        unpaintedTiles.clear();
        BlockPos tilePos;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (tileMap[height -i-1][width -j-1] > 0)  {
                    tilePos = levelOrigin.offset(j, 0, i);
                    unpaintedTiles.add(tilePos);
                }
            }
        }
    }

    protected void buildMap(int prevHeight, int prevWidth) {
        if (prevHeight > height || prevWidth > width) {
            int heightDiff = prevHeight - height;
            int widthDiff = prevWidth - width;
            BlockPos origin = getLevelOrigin(prevHeight, -1, prevWidth);
            BlockPos currPos = origin;
            for (int i = 0; i < prevHeight; i++) {
                for (int j = 0; j < prevWidth; j++) {
                    if (j == widthDiff && (i >= heightDiff && i <= prevHeight - heightDiff)) {
                        j += prevWidth - (2 * widthDiff);
                        currPos = currPos.offset(0, 0, prevWidth - (2 * widthDiff));
                        continue;
                    }
                    buildTile(currPos, -1);
                    buildTile(currPos.above(), 0);
                    currPos = currPos.offset(0, 0, 1);
                }
                currPos = currPos.offset(1, 0, origin.getZ() - currPos.getZ());
            }
        }
        resetMap();
    }

    protected void resetMap() {
        BlockPos currPos = levelOrigin;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                buildTile(currPos, tileMap[height -i-1][width -j-1]);
                buildTile(currPos.below(), -1);
                currPos = currPos.offset(1, 0, 0);
            }
            currPos = currPos.offset(levelOrigin.getX() - currPos.getX(), 0, 1);
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

    private BlockPos getLevelOrigin(int height, int y, int width) {
        return new BlockPos(gameOrigin.getX() - height/2, gameOrigin.getY() + y, gameOrigin.getZ() - width/2);
    }
}
