package tfar.davespotioneering.net;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.msrandom.multiplatform.annotations.Actual;
import net.neoforged.neoforge.network.PacketDistributor;

public class NetworkingHelperActual {
    @Actual
    public static void sendCycleGauntletPacket(boolean up) {
        PacketDistributor.sendToServer(new C2SGauntletCyclePacket(up));
    }

    @Actual
    public static void sendGauntletCooldownsPacket(ServerPlayer player, int[] cooldowns) {
        PacketDistributor.sendToPlayer(player, new S2CGauntletCooldownsPacket(cooldowns));
    }

    @Actual
    public static void sendPotionInjectorPacket(int button) {
        PacketDistributor.sendToServer(new C2SPotionInjectorPacket(button));
    }
}
