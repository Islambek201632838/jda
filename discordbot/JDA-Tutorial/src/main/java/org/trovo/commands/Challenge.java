package org.trovo.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.trovo.database.Dbase;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Challenge extends ListenerAdapter {
    private final Dbase challengeManager = new Dbase();
    private final Map<String, String> challengeMap = new HashMap<>();


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();
        User responder = event.getUser(); // The user who clicked the button (the challenged user)
        String challengeId;

        if (componentId.startsWith("accept_challenge_")) {
            challengeId = componentId.substring("accept_challenge_".length());
            handleChallengeResponse(event, challengeId, true);
        } else if (componentId.startsWith("reject_challenge_")) {
            challengeId = componentId.substring("reject_challenge_".length());
            handleChallengeResponse(event, challengeId, false);
        }
    }

    private void handleChallengeResponse(ButtonInteractionEvent event, String challengeId, boolean accepted) {
        Dbase.ChallengeData challenge = challengeManager.getChallenge(challengeId);
        if (challenge != null) {
            String challengerId = challenge.getChallengerId();
            String status = accepted ? "accepted" : "rejected";
            String date = challenge.getDate();

            // Inform the challenged user of the result
            event.reply("You have " + status + " the challenge on" + date).setEphemeral(true).queue();

            // Inform the challenger that the challenge was accepted or rejected
            event.getJDA().retrieveUserById(challengerId).queue(challenger -> {
                challenger.openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessageFormat("%s has %s your challenge scheduled for %s.", event.getUser().getAsTag(), status, date).queue();
                }, throwable -> {
                    // Handle the case where the private channel cannot be opened
                    System.err.println("Could not open a private channel with the challenger: " + throwable.getMessage());
                });
            }, throwable -> {
                // Handle the case where the challenger cannot be retrieved
                System.err.println("Could not retrieve the user by ID: " + throwable.getMessage());
            });

            // Remove the challenge if rejected or if further actions need to be taken for an accepted challenge
            challengeManager.removeChallenge(challengeId);
        } else {
            event.reply("Challenge does not exist or has already been handled.").setEphemeral(true).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

        if ("challenge_modal".equals(event.getModalId())) {
            String usernameInput = event.getValue("player_username").getAsString();
            String challengerId = event.getUser().getId();
            String challengeDate = event.getValue("challenge_date").getAsString();

            List<Member> matchedMembers = event.getGuild().getMembersByName(usernameInput, true);

            if (matchedMembers.isEmpty()) {
                // No members matched by username, try by nickname
                matchedMembers = event.getGuild().getMembersByNickname(usernameInput, true);
            }
            if (matchedMembers.isEmpty()) {
                // No members found by username or nickname
                event.reply("Could not find a user with the username or nickname: " + usernameInput)
                        .setEphemeral(true).queue();
            } else {
                // Handle the case where multiple members might match
                // For this example, we're just using the first match
                String challengeId = challengeManager.createChallenge(challengerId, usernameInput, challengeDate);; // need to create ticket generator
                User challengedUser = matchedMembers.get(0).getUser();
                challengedUser.openPrivateChannel().queue(privateChannel -> {
                    // Include the challenge date in the message to the challenged user
                    String messageToChallenged = String.format("You have been challenged by %s for a match on %s.",
                            event.getUser().getAsTag(), challengeDate);
                    privateChannel.sendMessage(messageToChallenged)
                            .setActionRow(
                                    Button.success("accept_challenge_" + challengeId, "Accept"),
                                    Button.danger("reject_challenge_" + challengeId, "Reject")
                            ).queue();
                    // Include the challenge date in the confirmation message to the challenger
                    String confirmationToChallenger = String.format("You've challenged %s for a match on %s.",
                            challengedUser.getAsTag(), challengeDate);
                    event.reply(confirmationToChallenger)
                            .setEphemeral(true).queue();
                }, throwable -> {
                    event.reply("Failed to send a challenge to the user. They might have DMs disabled.")
                            .setEphemeral(true).queue();
                });
            }
        }
    }
}
