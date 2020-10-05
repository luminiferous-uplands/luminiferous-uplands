package robosky.uplands.mixin;

//@Mixin(LocateCommand.class)
//public abstract class LocateCommandMixin {
//
//    @Shadow
//    private static native int execute(ServerCommandSource src, String arg) throws CommandSyntaxException;
//
//    @ModifyArg(
//        method = "register",
//        at = @At(
//          value = "INVOKE",
//          target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;",
//          remap = false
//        )
//    )
//    private static LiteralArgumentBuilder<ServerCommandSource> modifyLocate(LiteralArgumentBuilder<ServerCommandSource> cmd) {
//        return cmd.then(CommandManager.literal("Uplands_Megadungeon")
//            .executes(ctx -> execute(ctx.getSource(), "Uplands_Megadungeon")));
//    }
//}
