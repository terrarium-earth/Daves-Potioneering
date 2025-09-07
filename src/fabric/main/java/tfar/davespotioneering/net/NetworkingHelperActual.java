package tfar.davespotioneering.net;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.msrandom.multiplatform.annotations.Actual;

public class NetworkingHelperActual {

    @Actual
    public static void sendCycleGauntletPacket(boolean up) {
        ClientPlayNetworking.send(new C2SGauntletCyclePacket(up));
    }

    @Actual
    public static void sendGauntletCooldownsPacket(ServerPlayer player, int[] cooldowns) {
        ServerPlayNetworking.send(player, new S2CGauntletCooldownsPacket(cooldowns));
    }

    @Actual
    public static void sendPotionInjectorPacket(int button) {
        ClientPlayNetworking.send(new C2SPotionInjectorPacket(button));
    }
}
