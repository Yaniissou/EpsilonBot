package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.example.utils.HttpUtils;

import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

public class AnnonceCommand extends ListenerAdapter {


    private JDA jda;
    public AnnonceCommand(JDA jda){
        this.jda = jda;
    }
   @Override
   public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
       if (event.getName().equals("annonce")) {

           if (!Main.WHITELISTED_IDS.contains(event.getMember().getIdLong()) && !event.getMember().getRoles().stream().map(role -> role.getIdLong()).collect(Collectors.toList()).contains(Main.HOST_ROLE_ID)){
               event.reply("Vous ne pouvez pas utiliser cette commande.").setEphemeral(true).queue();
               return;
           }
           if (event.getGuild().getIdLong() != Main.SERVER_ID){
               event.reply("Cette commande est désactivée sur ce serveur.").setEphemeral(true).queue();
               return;
           }
           final MessageChannelUnion channel = event.getChannel();
           if (!Main.ANNOUNCE_CHANNEL_IDS.contains(channel.getIdLong())){
               event.reply("Ce salon n'est pas un salon d'annonce").setEphemeral(true).queue();
               return;
           }
           //infos given by command
           String date = event.getOption("date").getAsString();
           String mdj = event.getOption("mdj").getAsString();
           String maxPlayers = event.getOption("slots").getAsString();
           String description = event.getOption("description").getAsString();

           int game = HttpUtils.createGame(event.getMember().getUser().getName(), mdj, Integer.parseInt(maxPlayers), date);

           if (game != 201){
               event.reply("Une erreur est survenue lors de la création de la partie.").setEphemeral(true).queue();
               return;
           }


           long userID = event.getMember().getIdLong();
           User user = jda.retrieveUserById(userID).complete();

           //Clear logs channel
           MessageHistory history = MessageHistory.getHistoryFromBeginning(event.getGuild().getTextChannelById(Main.LOGS_CHANNEL_ID)).complete();
           history.getRetrievedHistory().forEach(m -> m.delete().queue());

           //Clear participants
           Main.PARTICIPANTS.clear();

           EmbedBuilder embed = new EmbedBuilder();
           final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
           final LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
           Instant instant = localDateTime.toInstant(ZoneOffset.of("+01:00"));
           embed.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
           embed.setColor(Color.RED);
           embed.setTitle("Annonce UHC");
           embed.setDescription("\uD83D\uDCD6 **Informations** \n" + description);
           embed.addField("\uD83D\uDD70\uFE0F **Date et heure**", String.format("<t:%s>, <t:%s:R>", instant.getEpochSecond(), instant.getEpochSecond()), true);
           embed.addField("\uD83C\uDF4E **Mode de jeu** ", mdj, true);
           embed.setFooter("Réagissez avec ✅ pour participer à la partie" , null);

           StringBuilder sb = new StringBuilder();
           Main.ROLES_TO_PING.forEach(roleid -> sb.append(event.getGuild().getRoleById(roleid).getAsMention()));
           channel.sendMessage(sb.toString()).queue();
           channel.sendMessageEmbeds(embed.build()).complete().addReaction(Emoji.fromUnicode("\u2705")).queue();
           channel.sendMessage("*Cette partie nécessite que vous soyez link* (</link:1282489633724305461>)").queue();

           event.reply("Annonce envoyée avec succès !").setEphemeral(true).queue();



       }
   }
}

