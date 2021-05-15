package au.natelev.amaze.game;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class Ball {
    private ServerWorld world;
    private Level inLevel;
    private BlockPos currPos;

    public Ball(Level level, ServerWorld world, BlockPos pos) {
        this.world = world;
        currPos = pos;
        inLevel = level;
        world.setBlockAndUpdate(currPos.below(), Blocks.BLUE_CONCRETE.defaultBlockState());
//        ballEntity = EntityType.BOAT.create(world);
//        world.addFreshEntity(ballEntity);
//        ballEntity.moveTo(currPos, 0, 0);
    }

    public void setCurrPos(int x, int y, int z) { setCurrPos(new BlockPos(x, y, z)); }
    public void setCurrPos(BlockPos pos) { currPos = pos; }

    public void moveUp() { while (move(currPos.south())); }
    public void moveDown() { while (move(currPos.north())); }
    public void moveLeft() { while (move(currPos.east())); }
    public void moveRight() { while (move(currPos.west())); }

    private boolean move(BlockPos newPos) {
        if (world.getBlockState(newPos).getBlock().equals(Blocks.AIR)) {
            world.setBlockAndUpdate(currPos.below(), Blocks.RED_CONCRETE.defaultBlockState());
            currPos = newPos;
            world.setBlockAndUpdate(currPos.below(), Blocks.BLUE_CONCRETE.defaultBlockState());
            return true;
        } else {
            return false;
        }
    }
}
