package org.example;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MessageListener extends ListenerAdapter {


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        if (event.getMessage().getContentRaw().endsWith("quoi")){
             event.getChannel().sendMessage(new Random().nextInt(2) == 1 ? "feur" : "coubeh").queue();
        } else if (event.getMessage().getAuthor().getId().equalsIgnoreCase("639734043906408448")){
            event.getChannel().sendMessage("Monkey").queue();
            event.getMessage().addReaction(Emoji.fromUnicode("\uD83D\uDC35")).queue();
        }
    }
}
