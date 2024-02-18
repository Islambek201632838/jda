package org.trovo.commands;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.JSONArray;
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
        String userName = event.getUser().getName();
        // List of game button IDs
        List<String> gameButtonIds = List.of(
                "dota2", "cs_go", "world_of_tanks", "pubg_mobile", "fortnite",
                "league_of_legends", "lineage", "escape_from_tarkov", "valorant",
                "fifa", "free_fire"
        );

        System.out.println("Button clicked: " + buttonId);
        if (gameButtonIds.contains(buttonId)) {

            String gameName = event.getButton().getLabel();
            try {
                String urlString = "http://127.0.0.1:8000/api/get_playmate/?user=" + URLEncoder.encode(userName, StandardCharsets.UTF_8) + "&gameName=" + URLEncoder.encode(gameName, StandardCharsets.UTF_8) + "&active=true";
                String response = HttpUtils.makeHttpRequest(urlString, "GET", null);

                if (response != null) {
                    try {
                        // Parse the JSON array
                        JSONArray jsonArray = new JSONArray(response);

                        // Loop through each JSON object in the array
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            // Extract the headline message from each JSON object
                            String headlineMessage = jsonObject.getString("headline_message");

                            // Handle the headline message
                            event.reply(headlineMessage).setEphemeral(true).queue();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Response is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
                event.reply("Failed to retrieve headline message").setEphemeral(true).queue();
            }
            event.reply("You've chosen: " + gameName).setEphemeral(true).queue();
        } else {
            switch (buttonId) {
                case "findPlaymate":
                    // Reply with the game buttons
                    event.reply("Choose your game:")
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
}