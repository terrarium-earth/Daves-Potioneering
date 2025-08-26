package tfar.davespotioneering.net;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.msrandom.multiplatform.annotations.Expect;

@SuppressWarnings("NoMatchingActual")
public class NetworkingHelper {

    @Expect
    public static void sendCycleGauntletPacket(boolean up);

    @Expect
    public static void sendGauntletCooldownsPacket(ServerPlayer player, int[] cooldowns);

    @Expect
    public static void sendPotionInjectorPacket(int button);
}
