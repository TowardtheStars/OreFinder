# 探矿镐

一个 Minecraft Mod, 增加了 5 种材料的探矿镐. 目前默认仅探测包含标签 "forge:ores" 的方块.

A Minecraft Mod adding Propicks of 5 materials which can detect ores for players. Ore finders' search range vary according to material enchantability. Now they detect blocks with tags containing "forge:ores" by defaults.

## 未来计划

- [ ] 与其他 mod 联动, 增加更多的材料.

  Be compatible to other mods, and add more materials.

- [x] 增加探测黑名单和白名单.

  Add blacklist and whitelist for detection.

  可以用数据包来添加探测白名单. 探矿镐将探测所有有标签 `#orefinder:prospect_blocks` 的方块.

  You can use a datapack to extend prospecting whitelist. Propicks will detect all blocks with tag `#orefinder:prospect_blocks`.

- [ ] 增加设置探矿范围和耐久值的配置项.

  Add a config to determine detection range and ore finders' durability.

- [ ] 增加一定概率无法探测到任何矿物的特性.

  Add a feature that ore finders might find nothing when there are ores around.

- [x] 增加探测冷却时间

  Add cooldown time for ore finders.
  
  现在探矿镐需要持续按住右键一段时间才能输出结果. 
  
  Now you need to keep using propicks for a short period of time until they yield result.