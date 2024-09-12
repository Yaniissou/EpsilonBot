package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.utils.HttpUtils;

import java.awt.*;
import java.util.List;

public class ReactionListener extends ListenerAdapter {

    private JDA jda;
    public ReactionListener(JDA jda){
        this.jda = jda;
    }


    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){
        if (event.getMember().getIdLong() == Main.BOT_ID){
            return;
        }
        if (event.getMessageAuthorIdLong() != (Main.BOT_ID)){
            return;
        }

        long userID = event.getUserIdLong();

        if (!Main.ANNOUNCE_CHANNEL_IDS.contains(event.getChannel().getIdLong())){
            return;
        }

        if (HttpUtils.getLinkedUsernames(List.of(userID)).isEmpty()){
            event.retrieveUser().complete().openPrivateChannel().complete().sendMessage("Vous devez lier votre compte pour participer à une partie. Pour cela, utilisez la commande </link:1282489633724305461>").queue();
            event.getReaction().removeReaction(event.getUser()).queue();
            return;
        }

        int response = HttpUtils.createGameUser(userID, HttpUtils.getLastGame());

        System.out.println(response);
        if (response == 406){
            event.retrieveUser().complete().openPrivateChannel().complete().sendMessage("La file d'attente est pleine !").queue();
            event.getReaction().removeReaction(event.getUser()).queue();
            return;
        }

        User user = jda.retrieveUserById(userID).complete();
        EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(Color.GREEN);
        embed.setTitle("Réaction ajoutée");
        embed.setDescription(user.getAsMention() + " a ajouté une réaction");
        embed.setThumbnail(user.getAvatarUrl());

        Main.PARTICIPANTS.add(userID);

        event.getGuild().getTextChannelById(Main.LOGS_CHANNEL_ID).sendMessageEmbeds(embed.build()).queue();

    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        if (event.getUserIdLong() == Main.BOT_ID){
            return;
        }
        if (event.retrieveMessage().complete().getAuthor().getIdLong() != (Main.BOT_ID)){
            return;
        }

        if (!Main.ANNOUNCE_CHANNEL_IDS.contains(event.getChannel().getIdLong())){
            return;
        }

        long userID = event.getUserIdLong();
        User user = jda.retrieveUserById(userID).complete();
        EmbedBuilder embed = new EmbedBuilder();

        HttpUtils.removeGameUser(userID,HttpUtils.getLastGame());
        embed.setColor(Color.RED);
        embed.setTitle("Réaction retirée");
        embed.setDescription(user.getAsMention() + " a retiré sa réaction");
        embed.setThumbnail(user.getAvatarUrl());

        Main.PARTICIPANTS.remove(userID);


        event.getGuild().getTextChannelById(Main.LOGS_CHANNEL_ID).sendMessageEmbeds(embed.build()).queue();

    }
}
