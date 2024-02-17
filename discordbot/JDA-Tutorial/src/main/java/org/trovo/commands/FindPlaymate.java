package org.trovo.commands;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.ArrayList;
import java.util.List;

public class FindPlaymate extends ListenerAdapter {

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if ("gameNameModal".equals(event.getModalId())) {
            String gameName = event.getValue("game_name").getAsString(); // Retrieve the game name from the modal
            // Process the game name (e.g., match with other players, store it, etc.)
            event.reply("You've entered the game name: " + gameName).setEphemeral(true).queue(); // Respond to the user
        }else if ("appeal".equals(event.getModalId())){
            String appealInfo = event.getValue("ticket").getAsString();
            event.reply("You've entered the game name: " + appealInfo).setEphemeral(true).queue();
        }
    }
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        System.out.println("Button clicked: " + buttonId);
        switch (buttonId) {
            case "findPlaymate":
                // Reply with the game buttons
                event.reply("Choose your game:")
                        .addActionRow(
                                Button.primary("dota2", "Dota 2"),
                                Button.primary("cs_go", "CS GO/CS 2"),
                                Button.primary("world_of_tanks", "World of Tanks"),
                                Button.primary("pubg_mobile", "PUBG Mobile"),
                                Button.primary("fortnite", "Fortnite")
                        )
                        .addActionRow(
                                Button.primary("league_of_legends", "League of Legends"),
                                Button.primary("lineage", "Lineage"),
                                Button.primary("escape_from_tarkov", "Escape from Tarkov"),
                                Button.primary("valorant", "Valorant"),
                                Button.primary("fifa", "FIFA")
                        )
                        .addActionRow(
                                Button.primary("free_fire", "Free Fire"),
                                Button.primary("others", "Others")
                        )
                        .setEphemeral(true)
                        .queue();
                break;
            case "appointments":
                event.reply("appoinments button clicked").setEphemeral(true).queue();
                break;
            case "appeal":
                TextInput subject = TextInput.create("ticket", "Ticket number", TextInputStyle.SHORT)
                        .setPlaceholder("Enter ticket number")
                        .setMinLength(10)
                        .setMaxLength(100) // or setRequiredRange(10, 100)
                        .build();

                TextInput body = TextInput.create("appeal_body", "Appeal", TextInputStyle.PARAGRAPH)
                        .setPlaceholder("Your concerns go here")
                        .setMinLength(30)
                        .setMaxLength(1000)
                        .build();
                Modal modal1 = Modal.create("appeal", "Appeal")
                        .addComponents(ActionRow.of(subject), ActionRow.of(body))
                        .build();

                event.replyModal(modal1).queue();
                break;
            case "others":
                // Prompt the user to enter the game name
                TextInput gameNameInput = TextInput.create("game_name", "Enter the game name", TextInputStyle.SHORT)
                        .setRequired(true)
                        .setPlaceholder("Type the game name here...")
                        .setMinLength(1)
                        .setMaxLength(100)
                        .build();

                Modal modal = Modal.create("gameNameModal", "Enter Game Name")
                        .addActionRows(ActionRow.of(gameNameInput))
                        .build();
                event.replyModal(modal).queue();
                break;
        }
    }



}