package au.natelev.amaze.game;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import static au.natelev.amaze.game.Game.*;

public class Ball {
    private final ServerWorld serverWorld;
    private Level inLevel = null;
    private BlockPos ballOrigin = null;
    private BlockPos currPos = null;

    public Ball(ServerWorld serverWorld) {
        this.serverWorld = serverWorld;
    }

    protected void moveDir(int dir) {
        for (boolean keepMoving = true; keepMoving;) {
            switch (dir) {
                case UP:
                    keepMoving = move(currPos.south());
                    break;
                case DOWN:
                    keepMoving = move(currPos.north());
                    break;
                case LEFT:
                    keepMoving = move(currPos.east());
                    break;
                case RIGHT:
                    keepMoving = move(currPos.west());
                    break;
            }
        }
    }

    protected void reset() {
        currPos = ballOrigin;
        serverWorld.setBlockAndUpdate(currPos.below(), Blocks.BLUE_CONCRETE.defaultBlockState());
        inLevel.paint(currPos);
    }

    protected void setLevel(Level level) {
        this.inLevel = level;
        this.ballOrigin = level.getBallOrigin();
        reset();
    }



    private boolean move(BlockPos newPos) {
        if (serverWorld.getBlockState(newPos).getBlock().equals(Blocks.AIR)) {
            serverWorld.setBlockAndUpdate(currPos.below(), Blocks.RED_CONCRETE.defaultBlockState());
            currPos = newPos;
            serverWorld.setBlockAndUpdate(currPos.below(), Blocks.BLUE_CONCRETE.defaultBlockState());
            inLevel.paint(newPos);
            return true;
        }
        return false;
    }
}
