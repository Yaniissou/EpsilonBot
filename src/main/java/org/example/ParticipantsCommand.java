package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.utils.HttpUtils;

import java.util.List;
import java.util.stream.Collectors;

public class ParticipantsCommand extends ListenerAdapter {

    private JDA jda;

    public ParticipantsCommand(JDA jda) {
        this.jda = jda;

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (!event.getName().equalsIgnoreCase("participants")) return;

        if (!Main.WHITELISTED_IDS.contains(event.getMember().getIdLong()) && !event.getMember().getRoles().stream().map(role -> role.getIdLong()).toList().contains(Main.HOST_ROLE_ID)) {
            event.reply("Vous ne pouvez pas utiliser cette commande.").setEphemeral(true).queue();
            return;
        }

        final List<String> participants = HttpUtils.getParticipants();

        event.reply("Participants: \n- " + String.join("\n - ", String.format("`%s`", participants))).queue();

    }
}