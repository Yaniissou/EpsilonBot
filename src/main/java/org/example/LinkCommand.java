package org.example;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.utils.HttpUtils;

public class LinkCommand extends ListenerAdapter {

    public LinkCommand() {

    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equalsIgnoreCase("link")) return;

        final String pseudo = event.getOption("pseudo").getAsString();

        int responseCode = HttpUtils.createUser(pseudo, event.getUser().getIdLong());

        if (responseCode == 201) {
            event.reply(String.format("Votre compte a bien été lié au pseudo `%s` !", pseudo)).setEphemeral(true).queue();
        } else {
            event.reply("Une erreur est survenue lors de la liaison de votre compte.").queue();
        }

    }
}
