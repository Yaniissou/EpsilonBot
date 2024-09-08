package org.example;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.utils.HttpUtils;

import java.util.List;
public class ParticipantsCommand extends ListenerAdapter {

    public ParticipantsCommand() {

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (!event.getName().equalsIgnoreCase("participants")) return;

        if (!Main.WHITELISTED_IDS.contains(event.getMember().getIdLong()) && !event.getMember().getRoles().stream().map(role -> role.getIdLong()).toList().contains(Main.HOST_ROLE_ID)){
            event.reply("Vous ne pouvez pas utiliser cette commande.").setEphemeral(true).queue();
            return;
        }

        List<String> usernames = HttpUtils.fetchUsernames(Main.PARTICIPANTS);
        event.reply("Participants: \n- " + String.join("\n - ", usernames)).queue();

    }
}