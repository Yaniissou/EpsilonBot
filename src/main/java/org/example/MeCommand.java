package org.example;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.utils.HttpUtils;

import java.util.Arrays;

public class MeCommand extends ListenerAdapter {
    public MeCommand() {
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("me")) return;

        final long discordID = event.getUser().getIdLong();
        final String pseudo = HttpUtils.getLinkedUsernames(Arrays.asList(discordID)).stream().findFirst().orElse(null);

        if (pseudo == null) {
            event.reply("Vous n'êtes pas lié à un compte Minecraft.").setEphemeral(true).queue();
        } else {
            event.reply(String.format("Votre pseudo Minecraft est `%s`.", pseudo)).setEphemeral(true).queue();
        }
    }
}
