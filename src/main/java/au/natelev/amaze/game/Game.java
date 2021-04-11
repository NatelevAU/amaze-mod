package au.natelev.amaze.game;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class Game {
    protected final MinecraftServer server;
    protected final ServerWorld serverWorld;
    protected Ball ball;

    private final List<Level> levels = new ArrayList<>();
    private Level currLevel;

    public Game(MinecraftServer server) {
        this.server = server;
        serverWorld = server.overworld();
        initLevels(levels);
    }

    private void initLevels(List<Level> levels) {
        levels.add(new Level(4, 4)); // add first level
        // TODO Init levels

        currLevel = levels.get(0);
        ball = new Ball(currLevel, serverWorld, new BlockPos(1, 65, -2));
    }

    public void moveUp() { ball.moveUp(); }
    public void moveDown() { ball.moveDown(); }
    public void moveLeft() { ball.moveLeft(); }
    public void moveRight() { ball.moveRight(); }
}
