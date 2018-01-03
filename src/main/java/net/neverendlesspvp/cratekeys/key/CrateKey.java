package net.neverendlesspvp.cratekeys.key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthew E on 1/3/2018.
 */
public class CrateKey {
    private final String name;
    private Map<String, Reward> rewardMap;

    public CrateKey(String name) {
        this.name = name;
        this.rewardMap = new HashMap<>();
    }

    public List<Reward> getRewardList() {
        return new ArrayList<>(rewardMap.values());
    }

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