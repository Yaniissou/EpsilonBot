package org.example;

import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MessageListener extends ListenerAdapter {


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event){
        if (event.getMessage().getContentRaw().endsWith("quoi")){
             event.getChannel().sendMessage(new Random().nextInt(2) == 1 ? "feur" : "coubeh").queue();

        }
    }
}
