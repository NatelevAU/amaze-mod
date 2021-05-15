package au.natelev.amaze.game;

import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final MinecraftServer server;
    private final ServerWorld serverWorld;
    private Ball ball;

    private final List<Level> levels = new ArrayList<>();
    private Level currLevel;

    private final BlockPos gameOrigin = new BlockPos(0, 65, 0);

    public Game(MinecraftServer server) {
        this.server = server;
        serverWorld = server.overworld();
        initLevels(levels);
    }

    private void initLevels(List<Level> levels) {
        Level firstLevel = new Level(6, 6, gameOrigin);
        int[][] firstLevelMap = {{0,0,0,0,0,0}, {0,2,1,1,2,0}, {0,1,0,0,1,0}, {0,1,0,0,1,0}, {0,1,1,1,2,0}, {0,0,0,0,0,0}};
        firstLevel.setTiles(firstLevelMap);
        levels.add(firstLevel); // add first level

        // TODO Init levels

        currLevel = levels.get(0);
        ball = new Ball(currLevel, serverWorld, new BlockPos(1, 65, -2));

        // Build first level
        firstLevel.buildMap(serverWorld, 0, 0);
    }

    public void moveUp() { ball.moveUp(); }
    public void moveDown() { ball.moveDown(); }
    public void moveLeft() { ball.moveLeft(); }
    public void moveRight() { ball.moveRight(); }

    public void reset() {
        BlockPos currPos = new BlockPos(1, 64, -1);
        currLevel.resetMap(serverWorld);
//        serverWorld.setBlockAndUpdate(currPos, Blocks.BLACK_CONCRETE.defaultBlockState()); currPos = currPos.south();
//        serverWorld.setBlockAndUpdate(currPos, Blocks.BLACK_CONCRETE.defaultBlockState()); currPos = currPos.south();
//        serverWorld.setBlockAndUpdate(currPos, Blocks.BLACK_CONCRETE.defaultBlockState()); currPos = currPos.west();
//        serverWorld.setBlockAndUpdate(currPos, Blocks.BLACK_CONCRETE.defaultBlockState()); currPos = currPos.west();
//        serverWorld.setBlockAndUpdate(currPos, Blocks.BLACK_CONCRETE.defaultBlockState()); currPos = currPos.west();
//        serverWorld.setBlockAndUpdate(currPos, Blocks.BLACK_CONCRETE.defaultBlockState()); currPos = currPos.north();
//        serverWorld.setBlockAndUpdate(currPos, Blocks.BLACK_CONCRETE.defaultBlockState()); currPos = currPos.north();
//        serverWorld.setBlockAndUpdate(currPos, Blocks.BLACK_CONCRETE.defaultBlockState()); currPos = currPos.north();
//        serverWorld.setBlockAndUpdate(currPos, Blocks.BLACK_CONCRETE.defaultBlockState()); currPos = currPos.east();
//        serverWorld.setBlockAndUpdate(currPos, Blocks.BLACK_CONCRETE.defaultBlockState()); currPos = currPos.east();
//        serverWorld.setBlockAndUpdate(currPos, Blocks.BLACK_CONCRETE.defaultBlockState()); currPos = currPos.east();
    }
}
