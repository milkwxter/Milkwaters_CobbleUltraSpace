package milkwater.cobbleultraspace.features;

import com.mojang.serialization.Codec;
import milkwater.cobbleultraspace.CobbleUltraSpace;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Optional;

public class FlexTreeBigFeature extends Feature<DefaultFeatureConfig> {
    public FlexTreeBigFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    private int GoThisBlocksDeep = 0;
    private String FeatureName = "flex_tree_big";

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        // get the world, spawnpoint, and structure manager
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        StructureTemplateManager mgr = world.getServer().getStructureTemplateManager();

        // get the structure
        Identifier id = Identifier.of(CobbleUltraSpace.MOD_ID, FeatureName);
        Optional<StructureTemplate> opt = mgr.getTemplate(id);
        if (opt.isEmpty()) {
            return false;
        }

        // if we found the structure, set some data
        StructureTemplate template = opt.get();
        StructurePlacementData data = new StructurePlacementData()
                .setIgnoreEntities(true)
                .setUpdateNeighbors(false)
                .setRotation(BlockRotation.random(world.getRandom()));

        // get the top block at the spawn coordinates
        BlockPos surface = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, origin);

        // set surface to the middle of the bottom of the structure
        surface = surface.add(
                -template.getSize().getX() / 2,
                0,
                -template.getSize().getZ() / 2
        );

        // only spawn if it's on top of a grass block
        Block blockBelow = world.getBlockState(surface.down()).getBlock();
        if (!(blockBelow == Blocks.GRASS_BLOCK)) {
            return false;
        }

        // place the structure
        BlockPos placePos = surface.add(0, -GoThisBlocksDeep,0);
        boolean success = template.place(world, placePos, placePos, data, world.getRandom(), Block.NOTIFY_LISTENERS);
        return success;
    }
}