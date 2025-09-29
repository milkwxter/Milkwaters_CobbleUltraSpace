package milkwater.ultraspace;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class TeleportHandler {
    public static int teleportToUltraSpace(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) return 0;

        ServerWorld ultraSpace = player.getServer().getWorld(CobbleUltraSpace.ULTRA_SPACE_KEY);
        if (ultraSpace != null) {
            player.teleport(ultraSpace, 0.0, 100.0, 0.0, player.getYaw(), player.getPitch());
            context.getSource().sendFeedback(
                    () -> Text.literal("Teleported to Ultra Space!"), false
            );
        } else {
            context.getSource().sendError(Text.literal("Ultra Space dimension not found!"));
        }
        return Command.SINGLE_SUCCESS;
    }
}
