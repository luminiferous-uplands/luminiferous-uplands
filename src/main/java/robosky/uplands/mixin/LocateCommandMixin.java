package robosky.uplands.mixin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.command.ServerCommandSource;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LocateCommand.class)
public abstract class LocateCommandMixin {

    @Shadow
    private static native int execute(ServerCommandSource src, String arg) throws CommandSyntaxException;

    @ModifyArg(
        method = "register",
        at = @At(
          value = "INVOKE",
          target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;",
          remap = false
        )
    )
    private static LiteralArgumentBuilder<ServerCommandSource> modifyLocate(LiteralArgumentBuilder<ServerCommandSource> cmd) {
        return cmd.then(CommandManager.literal("Uplands_Megadungeon")
            .executes(ctx -> execute(ctx.getSource(), "Uplands_Megadungeon")));
    }
}
