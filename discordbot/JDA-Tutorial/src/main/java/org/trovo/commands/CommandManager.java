package org.trovo.commands;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import java.util.ArrayList;
import java.util.List;
import org.trovo.commands.FindPlaymate;

public class CommandManager extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (command.equals("become_playmate")){
            Button messageButton = Button.primary("message", "Message");
            Button walletButton = Button.success("wallet", "Wallet");
            Button appointmentsButton = Button.primary("appointments", "Appointments");
            // Reply with buttons
            event.reply("Select an option:")
                    .addActionRow(messageButton, walletButton, appointmentsButton)
                    .setEphemeral(true)
                    .queue();
        }else if (command.equals("findplaymate")){
            Button findPlaymateButton = Button.primary("findPlaymate", "Find a Playmate");
            Button appointmentsButton = Button.success("appointments", "Appointments");
            Button appealButton = Button.primary("appeal", "Appeal");
            // Reply with buttons
            event.reply("Select an option:")
                    .addActionRow(findPlaymateButton, appointmentsButton, appealButton)
                    .setEphemeral(true)
                    .queue();
        }else if(command.equals("help")){
            event.reply("Welcome!!!\n1. /become_playmate - to register your account;\n2. /findplaymate - to start your journey;\n3. /help - to get details;")
                    .setEphemeral(true)
                    .queue();
        }
    }

    //Guild command
    @Override
    public void onGuildReady(GuildReadyEvent event) {
        super.onGuildReady(event);
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("become_playmate", "U need to register"));
        commandData.add(Commands.slash("findplaymate", "To find playmates"));
        commandData.add(Commands.slash("help", "To get details"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("become_playmate", "U need to register"));
        commandData.add(Commands.slash("findplaymate", "To find playmates"));
        commandData.add(Commands.slash("help", "To get details"));
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }

//    @Override
//    public void onReady(ReadyEvent event) {
//        List<CommandData> commandData = new ArrayList<>();
//        commandData.add(Commands.slash("welcome", "HiUhi"));
//        event.getJDA().updateCommands().addCommands(commandData).queue();
//    }
}


