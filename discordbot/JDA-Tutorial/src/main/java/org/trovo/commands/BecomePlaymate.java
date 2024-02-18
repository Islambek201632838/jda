package org.trovo.commands;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;




public class BecomePlaymate extends ListenerAdapter {

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

        if ("headlineModal".equals(event.getModalId())) {
            String headlineMessage = event.getValue("headline").getAsString();
            String userName = event.getUser().getName();
            String urlString = "http://localhost:8000/api/set_playmate/?user=" + URLEncoder.encode(userName, StandardCharsets.UTF_8);
            String requestBody = "{\"headline_message\": \"" + headlineMessage + "\"}";
            String response2= HttpUtils.makeHttpRequest(urlString, "PUT", requestBody);
            if (response2 != null) {
                System.out.println("Response: " + response2);

            } else {
                System.out.println("Failed to set headline message");
            }

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
                String userName = event.getUser().getName();
                String customGameName = event.getValue("customGameName").getAsString(); // Make sure this ID matches your TextInput ID
                String urlString = "http://localhost:8000/api/set_playmate/?user=" + URLEncoder.encode(userName, StandardCharsets.UTF_8);
                String requestBody = "{\"game_category\": \"" + customGameName + "\"}";
                String response= HttpUtils.makeHttpRequest(urlString, "PUT", requestBody);
                if (response != null) {
                    System.out.println("Response: " + response);

                } else {
                    System.out.println("Failed to set headline message");
                }
                // Respond to the user with the entered game name
                event.reply("You've entered the custom game name: " + customGameName).setEphemeral(true).queue();
            }
        }
    }

    public void onButtonInteraction(ButtonInteractionEvent event) {
        // Identify which button was clicked using the Button ID
        String buttonId = event.getComponentId();
        String userName = event.getUser().getName();
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

                try {
                    // Define the URL for the PUT request
                    String urlString = "http://localhost:8000/api/set_playmate/?user=" + URLEncoder.encode(userName, StandardCharsets.UTF_8);

                    // Prepare the JSON payload for the request
                    String jsonInputString = "{ \"game_category\": \"" + gameCategory + "\"}";

                    // Make the HTTP PUT request to set the game category
                    String response = HttpUtils.makeHttpRequest(urlString, "PUT", jsonInputString);

                    if (response != null) {
                        // Handle successful response
                        System.out.println("set_game_category successfully set");
                        event.reply("You selected the game category: " + gameCategory + " for your headline.").setEphemeral(true).queue();
                    } else {
                        // Handle unsuccessful response
                        System.out.println("Failed to set game category");
                        event.reply("Failed to set game category");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                    try {
                        String urlString2 = "http://127.0.0.1:8000/api/get_playmate/?user=" + URLEncoder.encode(userName, StandardCharsets.UTF_8);

                        String response2 = HttpUtils.makeHttpRequest(urlString2, "GET", null);

                        if (response2 != null) {

                            // Parse the JSON string
                            JSONObject jsonObject = new JSONObject(response2);

                            // Extract the headline message
                            String headlineMessage = jsonObject.getString("headline_message");

                            event.reply(headlineMessage).setEphemeral(true).queue();
                        } else {
                            System.out.println("GET request failed");
                            event.reply("Failed to retrieve headline message").setEphemeral(true).queue();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        event.reply("Failed to retrieve headline message").setEphemeral(true).queue();
                    }
                    break;
                case "sendMessage":
                    try {
                        // Define the URL for the PUT request
                        String urlString = "http://localhost:8000/api/set_playmate/?user=" + URLEncoder.encode(userName, StandardCharsets.UTF_8);
                        String requestBody =  "{ \"active\": true }";
                        String response2= HttpUtils.makeHttpRequest(urlString, "PUT", requestBody);
                        if (response2 != null) {
                            System.out.println("Response: " + response2);
                            event.reply("Message sent").setEphemeral(true).queue();

                        } else {
                            System.out.println("Failed to send");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

            }

        }
    }
}