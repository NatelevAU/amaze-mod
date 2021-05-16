package au.natelev.amaze.game;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Ball ball;

    private final List<Level> levels = new ArrayList<>();
    private Level currLevel = null;
    private int currLevelIndex = 0;

    private final BlockPos gameOrigin = new BlockPos(0, 65, 0);

    protected static final int UP = 0;
    protected static final int DOWN = 1;
    protected static final int LEFT = 2;
    protected static final int RIGHT = 3;

    public Game(MinecraftServer server) {
        initLevels(server.overworld());
        ball = new Ball(server.overworld());
        setLevel(levels.get(0));
    }

    private void initLevels(ServerWorld serverWorld) {
//        Level firstLevel = new Level(serverWorld,6, 6, gameOrigin);
//        int[][] firstLevelMap = {{0,0,0,0,0,0}, {0,2,1,1,2,0}, {0,1,0,0,1,0}, {0,1,0,0,1,0}, {0,1,1,1,2,0}, {0,0,0,0,0,0}};
//        firstLevel.setTiles(firstLevelMap);
//        levels.add(firstLevel); // add first level

        for (int i = 0; i < LevelData.levelMap.length; i++) {
            int[] metadata = LevelData.levelMetadata[i];
            int[][] map = LevelData.levelMap[i];
            Level level = new Level(serverWorld, metadata[0], metadata[1], gameOrigin);
            level.setTiles(map);
            levels.add(level);
        }
    }

    private void setLevel(Level level) {
        int prevWidth = 0, prevHeight = 0;
        if (currLevel != null) {
            prevWidth = currLevel.getWidth();
            prevHeight = currLevel.getHeight();
        }
        currLevel = level;
        level.buildMap(prevWidth, prevHeight);
        ball.setLevel(level);
    }

    private void nextLevel() {
        this.currLevelIndex++;
        if (currLevelIndex >= levels.size())  {
            currLevelIndex = 0;
        }
        setLevel(levels.get(currLevelIndex));
    }

    private void move(int dir) {
        ball.moveDir(dir);
        if (currLevel.isFinished()) {
            nextLevel();
        }
    }

    public void moveUp() { move(UP); }
    public void moveDown() { move(DOWN); }
    public void moveLeft() { move(LEFT); }
    public void moveRight() { move(RIGHT); }

    public void reset() {
        currLevel.resetMap();
        ball.reset();
    }
}
