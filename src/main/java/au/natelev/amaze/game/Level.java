package au.natelev.amaze.game;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final int width;
    private final int height;
    private int[][] tileMap;

    private final BlockPos gameOrigin;
    private final BlockPos levelOrigin;

    private Tile startTile;
    private final List<Tile> unpaintedTiles = new ArrayList<>();

    public Level(int width, int height, BlockPos gameOrigin) {
        this.width = width;
        this.height = height;
        this.gameOrigin = gameOrigin;
        this.levelOrigin = getLevelOrigin(width, 0, height);
    }



    protected int getWidth() { return this.width; }
    protected int getHeight() { return this.height; }



    protected Level setTiles(int[][] tileMap) {
        this.tileMap = tileMap;
        return this;
    }

    protected Level buildMap(ServerWorld serverWorld, int prevWidth, int prevHeight) {
        if (prevWidth > width || prevHeight > height) {
            int heightDiff = prevHeight - height;
            BlockPos currPos = getLevelOrigin(prevWidth, -1, prevHeight);
            for (int i = 0; i < prevWidth; i++) {
                for (int j = 0; j < prevHeight; j++) {
                    if (j == heightDiff) {
                        j += height - heightDiff;
                        currPos.offset(0, 0, height - heightDiff);
                        continue;
                    }
                    this.buildTile(serverWorld, currPos, 1);
                    currPos.offset(0, 0, 1);
                }
                currPos.offset(1, 0, levelOrigin.getZ() - currPos.getZ());
            }
        }

        return resetMap(serverWorld);
    }

    protected Level resetMap(ServerWorld serverWorld) {
        BlockPos currPos = levelOrigin;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                this.buildTile(serverWorld, currPos, tileMap[i][j]);
                this.buildTile(serverWorld, currPos.below(), -1);
                currPos.offset(0, 0, 1);
            }
            currPos.offset(1, 0, levelOrigin.getZ() - currPos.getZ());
        }
        return this;
    }



    private void buildTile(ServerWorld serverWorld, BlockPos pos, int tileState) {
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
