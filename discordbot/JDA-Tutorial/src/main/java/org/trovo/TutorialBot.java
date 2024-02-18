package org.trovo;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.trovo.commands.BecomePlaymate;
import org.trovo.commands.CommandManager;
import org.trovo.commands.FindPlaymate;
import org.trovo.listeners.EventListener;

import javax.security.auth.login.LoginException;

public class TutorialBot {

//    private final Dotenv config;

    private final ShardManager shardManager;


    public TutorialBot() throws LoginException {

//        config = Dotenv.configure().load();
        String token = "MTIwODQzMzk0MDA1MDg3NDQyOQ.GOW4Cm.TFzYIA0So0TGF1jsrsM1mk103Jw_sjGSdas5xg";
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.MESSAGE_CONTENT);

        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.watching("Aqua"));
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableCache(CacheFlag.ONLINE_STATUS);
        shardManager = builder.build();
        shardManager.addEventListener(new EventListener(), new CommandManager(), new FindPlaymate(), new BecomePlaymate());
    }

//    public Dotenv getConfig(){
//        return config;
//    }

    public ShardManager getShardManager(){
        return shardManager;
    }

    public static void main(String[] args) {
        try {
            TutorialBot bot = new TutorialBot();
        }catch (LoginException e){
            System.out.println("Error: Provided bot token is invalid");
        }
    }
}