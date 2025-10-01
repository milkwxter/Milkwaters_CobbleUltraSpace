package milkwater.cobbleultraspace.features;

import com.mojang.serialization.Codec;
import milkwater.cobbleultraspace.CobbleUltraSpace;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Optional;

public class FlexTreeFeature extends Feature<DefaultFeatureConfig> {
    public FlexTreeFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        // get the world, spawnpoint, and structure manager
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        StructureTemplateManager mgr = world.getServer().getStructureTemplateManager();

        // get the top block at the spawn coordinates
        BlockPos surface = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, origin);

        // only spawn if it's on top of a grass block
        Block blockBelow = world.getBlockState(surface.down()).getBlock();
        if (!(blockBelow == Blocks.GRASS_BLOCK)) {
            return false;
        }

        // get the structure
        Identifier id = Identifier.of(CobbleUltraSpace.MOD_ID, "flex_tree");
        Optional<StructureTemplate> opt = mgr.getTemplate(id);
        if (opt.isEmpty()) {
            System.out.println("CRAZY ERROR!!!! FlexTree: template manager couldn't load " + id);
            return false;
        }

        // if we found the structure, set some data
        StructureTemplate template = opt.get();
        StructurePlacementData data = new StructurePlacementData()
                .setIgnoreEntities(true)
                .setUpdateNeighbors(false);

        // place the structure
        BlockPos placePos = surface.add(
                -template.getSize().getX() / 2,
                0,
                -template.getSize().getZ() / 2
        );
        boolean success = template.place(world, placePos, placePos, data, world.getRandom(), Block.NOTIFY_LISTENERS);
        return success;
    }
}