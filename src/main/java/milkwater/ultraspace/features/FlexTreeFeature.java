package milkwater.ultraspace.features;

import com.mojang.serialization.Codec;
import milkwater.ultraspace.CobbleUltraSpace;
import net.minecraft.block.Block;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
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
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();

        // Snap to the top of terrain
        BlockPos surface = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, origin);

        Identifier id = Identifier.of(CobbleUltraSpace.MOD_ID, "flex_tree");

        Optional<StructureTemplate> opt = world.toServerWorld()
                .getStructureTemplateManager()
                .getTemplate(id);
        if (opt.isEmpty()) {
            System.out.println("FlexTree: template not found! Looking for " + id);
            return false;
        }

        StructureTemplate template = opt.get();
        StructurePlacementData data = new StructurePlacementData()
                .setIgnoreEntities(true)
                .setUpdateNeighbors(false);

        // Place directly on the surface
        BlockPos placePos = surface;

        boolean success = template.place(world, placePos, placePos, data, world.getRandom(), Block.NOTIFY_ALL);
        return success;
    }
}