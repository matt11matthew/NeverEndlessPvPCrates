package net.neverendlesspvp.cratekeys.key;

import net.neverendlesspvp.cratekeys.key.reward.Reward;
import net.neverendlesspvp.cratekeys.utilities.CrateItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class CrateKey {
    private String name;
    private CrateItem item;
    private Map<String, Reward> rewardMap;

    public CrateKey(String name, CrateItem item, Map<String, Reward> rewardMap) {
        this.name = name;
        this.item = item;
        this.rewardMap = rewardMap;
    }

    /**
     * Getter for property 'item'.
     *
     * @return Value for property 'item'.
     */
    public CrateItem getItem() {
        return item;
    }

    /**
     * Getter for property 'rewardMap'.
     *
     * @return Value for property 'rewardMap'.
     */
    public Map<String, Reward> getRewardMap() {
        return rewardMap;
    }


    /**
     * Getter for property 'rewardList'.
     *
     * @return Value for property 'rewardList'.
     */
    public List<Reward> getRewardList() {
        return new ArrayList<>(rewardMap.values());
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

}
/*
{
  "name": "TestKey",
  "item": {
    "type": "STONE",
    "data": 0,
    "displayName": "&aTest Key",
    "lore": [
      "&7Test Key"
    ]
  },
  "rewards": {
    "stoneReward": {
      "chance": 10.0,
      "commands": [
        {
          "chance": 5.0,
          "command": "give %player% stone 1"
        }
      ],
      "messages": {
        "broadcast": false,
        "winMessage": "You have won the stone reward from a test key",
        "broadcastMessage": "%player% has won the stone reward from a test key"
      },
      "item": {
        "type": "STONE",
        "data": 0,
        "displayName": "&aStone",
        "lore": [
          "&7Stone reward"
        ]
      }
    }
  }
}
 */