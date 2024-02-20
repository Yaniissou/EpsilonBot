package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class ReactionListener extends ListenerAdapter {


    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        if (event.getMember().getIdLong() == Main.BOT_ID){
            return;
        }
        if (event.getMessageAuthorIdLong() != (Main.BOT_ID)){
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(Color.GREEN);
        embed.setTitle("Réaction ajoutée");
        embed.setDescription(event.getMember().getAsMention() + " a ajouté une réaction");
        embed.setImage(event.getMember().getAvatarUrl());

        event.getGuild().getTextChannelById(Main.LOGS_CHANNEL_ID).sendMessageEmbeds(embed.build()).queue();

    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if (event.getMember().getIdLong() == Main.BOT_ID){
            return;
        }
        if (event.retrieveMessage().complete().getAuthor().getIdLong() != (Main.BOT_ID)){
            return;
        }

        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(Color.RED);
        embed.setTitle("Réaction retirée");
        embed.setDescription(event.getMember().getAsMention() + " a retiré sa réaction");
        embed.setImage(event.getMember().getAvatarUrl());

        event.getGuild().getTextChannelById(Main.LOGS_CHANNEL_ID).sendMessageEmbeds(embed.build()).queue();

    }
}
