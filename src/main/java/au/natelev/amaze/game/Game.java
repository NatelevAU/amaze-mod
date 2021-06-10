package au.natelev.amaze.game;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Ball ball;
    private final ServerWorld serverWorld;
    private final ServerBossInfo bossBar;

    private final List<Level> levels = new ArrayList<>();
    private Level currLevel = null;
    private int currLevelIndex = 0;
    private boolean wonLevel = false;

    private final BlockPos gameOrigin = new BlockPos(0, 165, 0);

    protected static final int UP = 0;
    protected static final int DOWN = 1;
    protected static final int LEFT = 2;
    protected static final int RIGHT = 3;

    public Game(MinecraftServer server) {
        this.serverWorld = server.overworld();
        this.ball = new Ball(server.overworld());
        this.bossBar = new ServerBossInfo(
                new StringTextComponent("Level " + Integer.toString(currLevelIndex + 1)),
                BossInfo.Color.BLUE, BossInfo.Overlay.byName("Level"));
        bossBar.setVisible(true);
        initLevels(levels);
        setLevel(server.overworld(), getLevelBlock(server.overworld()));
    }

    public void addPlayer(ServerPlayerEntity player) {
        bossBar.addPlayer(player);
    }

    private void bossBarSetLevel(int levelIndex) {
        bossBar.setName(new StringTextComponent("Level " + Integer.toString(levelIndex + 1)));
    }

    private void initLevels(List<Level> levels) {
        for (int i = 0; i < 104; i++) {
            int[] metadata = LevelData.levelMetadata[i];
            int[][] map = LevelData.getLevelMap(i);
            Level level = new Level(serverWorld, metadata[1], metadata[0], gameOrigin);
            level.setTiles(map);
            levels.add(level);
        }
    }

    public boolean isLevelWon() { return wonLevel; }

    private void setLevel(ServerWorld serverWorld, int levelIndex) {
        this.currLevelIndex = levelIndex;
        bossBarSetLevel(levelIndex);
        int prevHeight = 50, prevWidth = 50;
        int[] metadata = LevelData.levelMetadata[levelIndex];
        int[][] map = LevelData.getLevelMap(levelIndex);
        Level level = levels.get(levelIndex);
        currLevel = level;
        level.buildMap(prevHeight, prevWidth);
        ball.setLevel(level);
        setLevelBlock(serverWorld, levelIndex);
    }

    private void setLevelBlock(ServerWorld serverWorld, int levelIndex) {
        serverWorld.setBlockAndUpdate(new BlockPos(getLevelBlock(serverWorld), 0, 0), Blocks.AIR.defaultBlockState());
        serverWorld.setBlockAndUpdate(new BlockPos(levelIndex-1, 0, 0), Blocks.AIR.defaultBlockState());
        serverWorld.setBlockAndUpdate(new BlockPos(levelIndex, 0, 0), Blocks.STONE.defaultBlockState());
    }

    private int getLevelBlock(ServerWorld serverWorld) {
        BlockPos currPos = new BlockPos(0, 0, 0);
        for (int i = 0; i < 104; i++) {
            if (serverWorld.getBlockState(currPos).getBlock().equals(Blocks.STONE))
                return i;
            currPos = currPos.offset(1, 0, 0);
        }
        serverWorld.setBlockAndUpdate(new BlockPos(0, 0, 0), Blocks.STONE.defaultBlockState());
        return 0;
    }

    private void nextLevel() {
        if (isLevelWon()) return;
        wonLevel = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wonLevel = false;
        this.currLevelIndex++;
        if (currLevelIndex > 103)  {
            currLevelIndex--;
            resetLevel();
        } else {
            setLevel(serverWorld, currLevelIndex);
        }
    }

    private void move(int dir) {
        if (isLevelWon()) return;
        ball.moveDir(dir);
        if (currLevel.isFinished()) {
            Thread thread = new Thread(this::nextLevel);
            thread.start();
        }
    }

    public void moveUp() { move(UP); }
    public void moveDown() { move(DOWN); }
    public void moveLeft() { move(LEFT); }
    public void moveRight() { move(RIGHT); }

    public void reset() {
        if (isLevelWon()) return;
        currLevelIndex = 0;
        setLevel(serverWorld,0);
        resetLevel();
    }

    public void resetLevel() {
        if (isLevelWon()) return;
        currLevel.resetMap();
        ball.reset();
    }
}
