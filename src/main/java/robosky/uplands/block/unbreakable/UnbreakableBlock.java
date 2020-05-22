package robosky.uplands.block.unbreakable;

import net.minecraft.block.Block;

/**
 * A plain unbreakable block.
 */
public class UnbreakableBlock extends Block implements Unbreakable {

    private final Block base;

    public UnbreakableBlock(Block base) {
        super(Block.Settings.copy(base).strength(-1.0f, 3600000.0f));
        this.base = base;
    }

    /**
     * The block this ubreakable block is imitating.
     */
    @Override
    public Block base() {
        return base;
    }
}
