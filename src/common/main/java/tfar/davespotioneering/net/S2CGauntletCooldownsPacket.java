package tfar.davespotioneering.net;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import tfar.davespotioneering.DavesPotioneering;
import tfar.davespotioneering.client.GauntletHUDCommon;

public record S2CGauntletCooldownsPacket(int[] cooldowns) implements CustomPacketPayload {
    public static final ResourceLocation ID = DavesPotioneering.id("gauntlet_cooldowns");
    public static final Type<S2CGauntletCooldownsPacket> PACKET_TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, S2CGauntletCooldownsPacket> STREAM_CODEC = CustomPacketPayload.codec(
            S2CGauntletCooldownsPacket::write, S2CGauntletCooldownsPacket::new
    );

    private S2CGauntletCooldownsPacket(FriendlyByteBuf buf) {
        this(buf.readVarIntArray());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeVarIntArray(this.cooldowns());
    }

    public static void apply(S2CGauntletCooldownsPacket packet) {
        GauntletHUDCommon.cooldowns = packet.cooldowns();
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PACKET_TYPE;
    }
}
