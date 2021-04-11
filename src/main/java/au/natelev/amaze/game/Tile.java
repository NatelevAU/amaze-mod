package au.natelev.amaze.game;

import net.minecraft.util.math.BlockPos;

public class Tile {
    private BlockPos pos;
    private boolean painted;

    public int getX() { return pos.getX(); }
    public int getY() { return pos.getY(); }
    public int getZ() { return pos.getZ(); }
    public BlockPos getPos() { return pos; }

    public void paint() { painted = true; }
    public boolean isPainted() { return painted; }
}
