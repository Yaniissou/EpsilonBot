package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.sql.SQLException;

public class AnnonceCommand extends ListenerAdapter {


    private JDA jda;
    public AnnonceCommand(JDA jda){
        this.jda = jda;
    }
   @Override
   public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
       if (event.getName().equals("annonce")) {

           if (!Main.WHITELISTED_IDS.contains(event.getMember().getIdLong())){
               event.reply("Vous ne pouvez pas utiliser cette commande.").setEphemeral(true).queue();
               return;
           }
           if (event.getGuild().getIdLong() != Main.SERVER_ID){
               event.reply("Cette commande est désactivée sur ce serveur.").setEphemeral(true).queue();
               return;
           }
           if (!Main.ANNOUNCE_CHANNEL_IDS.contains(event.getChannel().getIdLong())){
               event.reply("Ce salon n'est pas un salon d'annonce").setEphemeral(true).queue();
               return;
           }
           //infos given by command
           String date = event.getOption("date").getAsString();
           String horaire = event.getOption("horaire").getAsString();
           String mdj = event.getOption("mdj").getAsString();
           String description = event.getOption("description").getAsString();


           long userID = event.getMember().getIdLong();
           User user = jda.retrieveUserById(userID).complete();

           //Clear logs channel
           MessageHistory history = MessageHistory.getHistoryFromBeginning(event.getGuild().getTextChannelById(Main.LOGS_CHANNEL_ID)).complete();
           history.getRetrievedHistory().forEach(m -> m.delete().queue());


           EmbedBuilder embed = new EmbedBuilder();
           embed.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
           embed.setColor(Color.RED);
           embed.setTitle("Annonce UHC");
           embed.setDescription("\uD83D\uDCD6 **Informations** \n" + description);
           embed.addField("\uD83D\uDD70\uFE0F **Date et heure**", date + " à " + horaire, true);
           embed.addField("\uD83C\uDF4E **Mode de jeu** ", mdj, true);
           embed.setFooter("Réagissez avec ✅ pour participer à la partie" , null);

           StringBuilder sb = new StringBuilder();
           Main.ROLES_TO_PING.forEach(roleid -> sb.append(event.getGuild().getRoleById(roleid).getAsMention()));
           event.getChannel().sendMessage(sb.toString()).queue();
           event.getChannel().sendMessageEmbeds(embed.build()).queue(message -> {
               message.addReaction(Emoji.fromUnicode("\u2705")).queue();
               message.createThreadChannel("Mettez vos pseudos ici !").queue();
               try {
                   DatabaseManager.connect("/app/db/botDB.db");
                   DatabaseManager.insertMessage(message.getId(),message.getAuthor().getId(),mdj);
                   DatabaseManager.close();
               } catch (SQLException e) {
                   throw new RuntimeException(e);
               }

           });
           event.reply("Annonce envoyée avec succès !").setEphemeral(true).queue();



       }
   }
}

