package org.trovo.commands;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;


public class BecomePlaymate extends ListenerAdapter {




    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if ("headlineModal".equals(event.getModalId())) {
            String headlineMessage = event.getValue("headline").getAsString();
            // Process the headline message, like storing it temporarily
            // Then prompt the user to select a game category
            event.reply("Please select a game category for your headline:")
                    .addActionRow(
                            Button.primary("game_dota2", "Dota 2"),
                            Button.primary("game_cs_go", "CS GO/CS 2"),
                            Button.primary("game_world_of_tanks", "World of Tanks"),
                            Button.primary("game_pubg_mobile", "PUBG Mobile"),
                            Button.primary("game_fortnite", "Fortnite")
                    )
                    .addActionRow(
                            Button.primary("game_league_of_legends", "League of Legends"),
                            Button.primary("game_lineage", "Lineage"),
                            Button.primary("game_escape_from_tarkov", "Escape from Tarkov"),
                            Button.primary("game_valorant", "Valorant"),
                            Button.primary("game_fifa", "FIFA")
                    )
                    .addActionRow(
                            Button.primary("game_free_fire", "Free Fire"),
                            Button.primary("game_others", "Others")
                    )
                    .setEphemeral(true)
                    .queue();
        }else{
            if ("enterGameNameModal".equals(event.getModalId())) {
                // Retrieve the custom game name entered by the user
                String customGameName = event.getValue("customGameName").getAsString(); // Make sure this ID matches your TextInput ID

                // Respond to the user with the entered game name
                event.reply("You've entered the custom game name: " + customGameName).setEphemeral(true).queue();
            }
        }
    }
    
    public void onButtonInteraction(ButtonInteractionEvent event) {
        // Identify which button was clicked using the Button ID
        String buttonId = event.getComponentId();
        if (buttonId.startsWith("game_")) {
            if ("game_others".equals(buttonId)) {
                // Create and show a modal for entering a custom game name
                TextInput customGameInput = TextInput.create("customGameName", "Custom Game Name", TextInputStyle.SHORT)
                        .setPlaceholder("Enter the custom game name here...")
                        .setRequired(true)
                        .build();
                Modal modal = Modal.create("enterGameNameModal", "Enter Custom Game Name")
                        .addActionRows(ActionRow.of(customGameInput))
                        .build();
                event.replyModal(modal).queue();
            } else {
                String gameCategory = event.getButton().getLabel();
                event.reply("You selected the game category: " + gameCategory + " for your headline.").setEphemeral(true).queue();
            }
        } else {
            switch (buttonId) {
                case "message":
                    // Acknowledge the button interaction and reply ephemeral
                    event.reply("What would you like to do with the message?")
                            .setEphemeral(true) // This makes the reply only visible to the user
                            .addActionRow(
                                    Button.primary("editMessage", "Edit Message"),
                                    Button.primary("headlineMessage", "Headline Message"),
                                    Button.success("sendMessage", "Send Message")
                            )
                            .queue();
                case "wallet":
                    event.reply("Wallet button clicked").setEphemeral(true).queue();
                    break;
                case "appointments":
                    event.reply("Appointments button clicked").setEphemeral(true).queue();
                    break;
                case "editMessage":
                    // Respond with "edit message printed" when "Edit Message" is clicked
                    // Create a text input for the headline message
                    TextInput headlineInput = TextInput.create("headline", "Headline", TextInputStyle.SHORT)
                            .setPlaceholder("Enter your headline message here...")
                            .setRequired(true)
                            .build();
                    // Create and show the modal
                    Modal modal = Modal.create("headlineModal", "Enter Headline Message")
                            .addActionRows(ActionRow.of(headlineInput))
                            .build();
                    event.replyModal(modal).queue();
                    break;
                case "headlineMessage":
                    // Respond with "edit message printed" when "Edit Message" is clicked
                    event.reply("Head line message printed").setEphemeral(true).queue();
                    break;
                case "sendMessage":
                    // Respond with "edit message printed" when "Edit Message" is clicked
                    event.reply("Send message printed").setEphemeral(true).queue();
                    break;
                case "game_cs_go":
                    // Extract the game category from the button ID
                    String gameCategory = buttonId.substring(5); // Removes "game_" prefix
                    // Process the selected game category along with the previously entered headline message
                    event.reply("You selected the game category: " + gameCategory + " for your headline.").setEphemeral(true).queue();
                    break;
            }

        }
    }
}
