package org.trovo.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.w3c.dom.Text;
import org.trovo.commands.FindPlaymate;

public class BotCommands extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        TextInput name = TextInput.create("player-name", "name", TextInputStyle.SHORT)
                .setMinLength(1)
                .setRequired(true)
                .build();

        TextInput message = TextInput.create("player-message", "Message", TextInputStyle.PARAGRAPH)
                .setMinLength(10)
                .setMaxLength(100)
                .setRequired(true)
                .setPlaceholder("Some message")
                .build();

        Modal modal = Modal.create("player-modal", "join")
                .addActionRows(ActionRow.of(name), ActionRow.of(message))
                .build();


        event.replyModal(modal).queue();

    }
}
