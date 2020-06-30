package cn.mappyj.action.skyblock;

import cn.mappyj.utils.GetFileTextUtil;
import cn.mappyj.utils.LanguageUtil;
import cn.mappyj.utils.MojangCastUtil;
import cn.mappyj.utils.RemoveNull;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.skyblock.SkyBlockProfileReply;
import org.meowy.cqp.jcq.entity.CoolQ;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.concurrent.ExecutionException;

public class GetSkyBlockInfo extends SkyBlockJson{

    private final String ID;

    public GetSkyBlockInfo(long GroupID, CoolQ CQ, HypixelAPI apiKey, String... args) throws InterruptedException, ExecutionException, IOException {
        super(GroupID, CQ, apiKey, args);
        this.ID = args[0];
        super.uuid = getStringUUID();
        if(!isnull(super.uuid)){
            super.file = getFile();
            execute();
        }
    }

    @Override
    protected void execute() throws IOException, ExecutionException, InterruptedException {
        String profile_ID = getprofileid();
        String profile_Name = getprofilename();
        if(isnull(profile_ID))return;
        SkyBlockProfileReply profileReply = apiKey.getSkyBlockProfile(profile_ID).get();
        if(isnull(profileReply.getProfile())){CQ.sendGroupMsg(GroupID,"该账号绑定存档不存在!请重新绑定!");return;}
        JsonObject
                self = profileReply.getProfile().get("members").getAsJsonObject().get(super.uuid).getAsJsonObject(),
                selfStats = self.get("stats").getAsJsonObject();
        JsonElement
                json_Purse = self.get("coin_purse"),
                json_FairySouls = self.get("fairy_souls_collected"),
                json_FishingTreasure = self.get("fishing_treasure_caught"),
                json_Deaths = selfStats.get("deaths"),
                json_Kills = selfStats.get("kills"),
                json_KillsOldDragon = selfStats.get("kills_wise_dragon"),
                json_KillsProtectorDragon = selfStats.get("kills_protector_dragon"),
                json_KillsYoungDragon = selfStats.get("kills_young_dragon"),
                json_KillsStrongDragon = selfStats.get("kills_strong_dragon"),
                json_KillsSuperiorDragon = selfStats.get("kills_superior_dragon"),
                json_KillsUnstableDragon = selfStats.get("kills_unstable_dragon"),
                json_KillsWiseDragon = selfStats.get("kills_wise_dragon"),
                json_SlayerWolf = getSlayerLevelKind(self,"wolf"),
                json_SlayerZombie = getSlayerLevelKind(self,"zombie"),
                json_SlayerSpider = getSlayerLevelKind(self,"spider"),
                json_Pets = self.get("pets");

        double
                Purse = isnull(json_Purse)?0:json_Purse.getAsDouble(),
                bank_deposit = isnull(profileReply.getProfile().get("banking"))?0:isnull(profileReply.getProfile().get("banking").getAsJsonObject().get("balance"))?0:profileReply.getProfile().get("banking").getAsJsonObject().get("balance").getAsDouble();
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        int
                FairySouls = isnull(json_FairySouls)?0:json_FairySouls.getAsInt(),
                FishingTreasure = isnull(json_FishingTreasure)?0:json_FishingTreasure.getAsInt(),
                Deaths = isnull(json_Deaths)?0:json_Deaths.getAsInt(),
                Kills = isnull(json_Kills)?0:json_Kills.getAsInt(),
                KillsOldDragon = isnull(json_KillsOldDragon)?0:json_KillsOldDragon.getAsInt(),
                KillsYoungDragon = isnull(json_KillsYoungDragon)?0:json_KillsYoungDragon.getAsInt(),
                KillsProtectorDragon = isnull(json_KillsProtectorDragon)?0:json_KillsProtectorDragon.getAsInt(),
                KillsStrongDragon = isnull(json_KillsStrongDragon)?0:json_KillsStrongDragon.getAsInt(),
                KillsSuperiorDragon = isnull(json_KillsSuperiorDragon)?0:json_KillsSuperiorDragon.getAsInt(),
                KillsUnstableDragon = isnull(json_KillsUnstableDragon)?0:json_KillsUnstableDragon.getAsInt(),
                KillsWiseDragon = isnull(json_KillsWiseDragon)?0:json_KillsWiseDragon.getAsInt(),
                SlayerWolf = isnull(json_SlayerWolf)?0:json_SlayerWolf.getAsJsonObject().entrySet().size(),
                SlayerSpider = isnull(json_SlayerSpider)?0:json_SlayerSpider.getAsJsonObject().entrySet().size(),
                SlayerZombie = isnull(json_SlayerZombie)?0:json_SlayerZombie.getAsJsonObject().entrySet().size();
        String[] temp_LegPets = returnLegPets(json_Pets),
                LegPets = isnull(temp_LegPets)?new String[]{LanguageUtil.Unknown}:new RemoveNull().deleteArrayNull(temp_LegPets);
        String temp_ActivePet = returnActivePet(json_Pets),
                ActivePet = isnull(temp_ActivePet)?LanguageUtil.Unknown:temp_ActivePet;

        StringBuilder msg = new StringBuilder();
        /*
        [Hypixel]玩家hypixel的SkyBlock信息:
        所选存档:CuteName  个人余额:22
        银行存款:111111
        灵魂收集:22  钓鱼宝藏:222
        击杀:22222  死亡:222222
        Slayer任务:
        Zombie:Lvl2  Spider:Lvl3
        Wolf:Lvl4
        击杀龙(自己放眼):
        Old:2  Unstable:2  Wise:4
        Protector:3  Young:3  Strong:2
        Superior:3
        当前宠物:HORSE
        拥有传奇宠物:HORSE,NIMM
         */

        msg.append("[Hypixel]玩家").append(ID).append("的SkyBlock信息:")
                .append("\n").append("所选存档:").append(profile_Name).append("  ").append("个人余额:").append(format.format(Purse)).append("$")
                .append("\n").append("银行存款:").append(format.format(bank_deposit)).append("$")
                .append("\n").append("灵魂收集:").append(FairySouls).append("  ").append("钓鱼宝藏:").append(FishingTreasure)
                .append("\n").append("击杀:").append(Kills).append("  ").append("死亡:").append(Deaths)
                .append("\n").append("Slayer任务:").append("\n").append("Zombie:Lvl").append(SlayerZombie).append("  ").append("Spider:Lvl").append(SlayerSpider)
                .append("\n").append("Wolf:Lvl").append(SlayerWolf)
                .append("\n").append("击杀龙(自己召唤):")
                .append("\n").append("Old:").append(KillsOldDragon).append("  ").append("Unstable:").append(KillsUnstableDragon).append("  ").append("Wise:").append(KillsWiseDragon)
                .append("\n").append("Protector:").append(KillsProtectorDragon).append("  ").append("Young:").append(KillsYoungDragon).append("  ").append("Strong:").append(KillsStrongDragon)
                .append("\n").append("Superior:").append(KillsSuperiorDragon)
                .append("\n").append("当前宠物:").append(ActivePet)
                .append("\n").append("拥有传奇宠物:").append(String.join(",", LegPets));
        CQ.sendGroupMsg(GroupID,msg.toString());
    }

    private String getprofileid(){
        try {
            perJson = new GetFileTextUtil().getJson(super.file);
            if(isnull(perJson)||isnull(super.perJson.get("profile_id"))){CQ.sendGroupMsg(GroupID,"请输入/hyp sb ID list 查看存档列表后\n输入/hyp sb ID bound [序号]进行绑定");return null;}
            return super.perJson.get("profile_id").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
        }
        return null;
    }

    private String getprofilename(){
        try {
            perJson = new GetFileTextUtil().getJson(super.file);
            if(isnull(perJson)||isnull(super.perJson.get("profile_name")))return null;
            return super.perJson.get("profile_name").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
            CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
        }
        return null;
    }

    private JsonObject getSlayerLevelKind(JsonObject self,String kind){
        return isnull(self.get("slayer_bosses"))?null:isnull(self.get("slayer_bosses").getAsJsonObject().get(kind).getAsJsonObject().get("claimed_levels"))?null:self.get("slayer_bosses").getAsJsonObject().get(kind).getAsJsonObject().get("claimed_levels").getAsJsonObject();
    }

    private String returnActivePet(JsonElement Pets){
        if (isnull(Pets)) return null;
        JsonArray PetsListArray = Pets.getAsJsonArray();
        String Pet;
        for(JsonElement PerPetsElements:PetsListArray){
            JsonObject PerPets = PerPetsElements.getAsJsonObject();
            if(PerPets.get("active").getAsBoolean()){
                Pet = PerPets.get("type").getAsString();
                return Pet;
            }
        }
        return null;
    }

    private String[] returnLegPets(JsonElement Pets){
        if (isnull(Pets)) return null;
        JsonArray PetsListArray = Pets.getAsJsonArray();
        String[] PetsList = new String[PetsListArray.size()];
        int temp = 0;
        for(JsonElement PerPetsElements:PetsListArray){
            JsonObject PerPets = PerPetsElements.getAsJsonObject();
            if(PerPets.get("tier").getAsString().equals("LEGENDARY")){
                PetsList[temp] = PerPets.get("type").getAsString();
                temp++;
            }
        }
        return PetsList;
    }

    private String getStringUUID(){
        try{
            MojangCastUtil mojangCastUtil = new MojangCastUtil();
            String Stringuuid =  mojangCastUtil.nametoStringUUID(this.ID);
            if(isnull(Stringuuid)){
                CQ.sendGroupMsg(GroupID, LanguageUtil.Mojang_InvalidName);
                return null;
            }else{
                return Stringuuid;
            }
        }catch(IOException e){
            CQ.sendGroupMsg(GroupID,LanguageUtil.CatchException);
            e.printStackTrace();
        }
        return null;
    }
}
